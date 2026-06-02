#!/usr/bin/env node
const fs = require("fs");
const path = require("path");
const zlib = require("zlib");

const CONTENT_WIDTH = 9360;
const W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
const R_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
let usedNumbering = [];

function x(text) {
  return String(text || "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

function inlineParts(text) {
  const parts = [];
  const re = /`([^`]+)`|\*\*([^*]+)\*\*/g;
  let pos = 0;
  let match;
  while ((match = re.exec(text)) !== null) {
    if (match.index > pos) parts.push(["text", text.slice(pos, match.index)]);
    parts.push(match[1] ? ["code", match[1]] : ["bold", match[2]]);
    pos = re.lastIndex;
  }
  if (pos < text.length) parts.push(["text", text.slice(pos)]);
  return parts;
}

function run(text, kind = "text") {
  const props = [];
  if (kind === "bold") props.push("<w:b/>");
  if (kind === "code") {
    props.push('<w:rFonts w:ascii="Consolas" w:hAnsi="Consolas" w:eastAsia="Microsoft YaHei"/>');
    props.push('<w:color w:val="44546A"/>');
  }
  const pr = props.length ? `<w:rPr>${props.join("")}</w:rPr>` : "";
  return `<w:r>${pr}<w:t xml:space="preserve">${x(text)}</w:t></w:r>`;
}

function para(text = "", style = null, opts = {}) {
  const ppr = [];
  if (style) ppr.push(`<w:pStyle w:val="${style}"/>`);
  const spacing = [];
  if (opts.before != null) spacing.push(`w:before="${opts.before}"`);
  if (opts.after != null) spacing.push(`w:after="${opts.after}"`);
  if (spacing.length) ppr.push(`<w:spacing ${spacing.join(" ")}/>`);
  const pprXml = ppr.length ? `<w:pPr>${ppr.join("")}</w:pPr>` : "";
  const content = opts.bold
    ? run(text, "bold")
    : inlineParts(text).map(([kind, value]) => run(value, kind)).join("");
  return `<w:p>${pprXml}${content}</w:p>`;
}

function listPara(text, numId) {
  return `<w:p><w:pPr><w:pStyle w:val="ListParagraph"/><w:numPr><w:ilvl w:val="0"/><w:numId w:val="${numId}"/></w:numPr><w:spacing w:after="80" w:line="280" w:lineRule="auto"/></w:pPr>${inlineParts(text).map(([kind, value]) => run(value, kind)).join("")}</w:p>`;
}

function table(rows) {
  if (!rows.length) return "";
  const cols = Math.max(...rows.map((r) => r.length));
  const base = Math.floor(CONTENT_WIDTH / cols);
  const widths = Array(cols).fill(base);
  widths[cols - 1] += CONTENT_WIDTH - widths.reduce((a, b) => a + b, 0);
  let out = `<w:tbl><w:tblPr><w:tblW w:w="9360" w:type="dxa"/><w:tblInd w:w="120" w:type="dxa"/><w:tblLayout w:type="fixed"/><w:tblBorders><w:top w:val="single" w:sz="4" w:color="BFBFBF"/><w:left w:val="single" w:sz="4" w:color="BFBFBF"/><w:bottom w:val="single" w:sz="4" w:color="BFBFBF"/><w:right w:val="single" w:sz="4" w:color="BFBFBF"/><w:insideH w:val="single" w:sz="4" w:color="D9E2F3"/><w:insideV w:val="single" w:sz="4" w:color="D9E2F3"/></w:tblBorders><w:tblCellMar><w:top w:w="100" w:type="dxa"/><w:left w:w="120" w:type="dxa"/><w:bottom w:w="100" w:type="dxa"/><w:right w:w="120" w:type="dxa"/></w:tblCellMar></w:tblPr><w:tblGrid>${widths.map((w) => `<w:gridCol w:w="${w}"/>`).join("")}</w:tblGrid>`;
  rows.forEach((row, ri) => {
    out += "<w:tr>";
    for (let ci = 0; ci < cols; ci++) {
      const fill = ri === 0 ? '<w:shd w:fill="F2F4F7"/>' : "";
      out += `<w:tc><w:tcPr><w:tcW w:w="${widths[ci]}" w:type="dxa"/><w:vAlign w:val="center"/>${fill}</w:tcPr>${para(row[ci] || "", ri === 0 ? "TableHeader" : "TableText")}</w:tc>`;
    }
    out += "</w:tr>";
  });
  return `${out}</w:tbl>${para("", null, { after: 80 })}`;
}

function parseMarkdown(markdown) {
  const lines = markdown.split(/\r?\n/);
  const body = [];
  let i = 0;
  let nextNumId = 10;
  let activeList = null;
  let activeNumId = null;
  const ensureList = (kind) => {
    if (activeList !== kind) {
      activeList = kind;
      activeNumId = nextNumId++;
      usedNumbering.push({ numId: activeNumId, kind });
    }
    return activeNumId;
  };
  const stopList = () => {
    activeList = null;
    activeNumId = null;
  };
  while (i < lines.length) {
    const line = lines[i].trimEnd();
    if (!line.trim()) {
      stopList();
      i++;
      continue;
    }
    if (line.trim().startsWith("|") && line.trim().slice(1).includes("|")) {
      const rows = [];
      while (i < lines.length && lines[i].trim().startsWith("|")) {
        const cells = lines[i].trim().replace(/^\||\|$/g, "").split("|").map((c) => c.trim());
        if (!cells.every((c) => /^:?-{2,}:?$/.test(c || ""))) rows.push(cells);
        i++;
      }
      body.push(table(rows));
      stopList();
      continue;
    }
    const heading = /^(#{1,6})\s+(.*)$/.exec(line);
    if (heading) {
      const level = heading[1].length;
      const text = heading[2].trim();
      if (level === 1) {
        body.push(para(text, "Title"));
        body.push(para("项目实训课程报告", "Subtitle"));
      } else if (level === 2) body.push(para(text, "Heading1"));
      else if (level === 3) body.push(para(text, "Heading2"));
      else body.push(para(text, "Heading3"));
      stopList();
      i++;
      continue;
    }
    const numbered = /^\d+\.\s+(.*)$/.exec(line);
    if (numbered) {
      body.push(listPara(numbered[1], ensureList("ordered")));
      i++;
      continue;
    }
    const bullet = /^[-*]\s+(.*)$/.exec(line);
    if (bullet) {
      body.push(listPara(bullet[1], ensureList("bullet")));
      i++;
      continue;
    }
    stopList();
    const parts = [line.trim()];
    i++;
    while (i < lines.length && lines[i].trim() && !/^(#{1,6})\s+|^\d+\.\s+|^[-*]\s+|^\|/.test(lines[i].trim())) {
      parts.push(lines[i].trim());
      i++;
    }
    body.push(para(parts.join(" "), "Normal"));
  }
  return body.join("");
}

function styles() {
  const style = (id, name, size, color = "000000", bold = false, before = 0, after = 120, line = 280, based = "Normal") => {
    const base = id === "Normal" ? "" : `<w:basedOn w:val="${based}"/>`;
    return `<w:style w:type="paragraph" w:styleId="${id}"><w:name w:val="${name}"/>${base}<w:pPr><w:spacing w:before="${before}" w:after="${after}" w:line="${line}" w:lineRule="auto"/></w:pPr><w:rPr><w:rFonts w:ascii="Calibri" w:hAnsi="Calibri" w:eastAsia="Microsoft YaHei"/>${bold ? "<w:b/>" : ""}<w:color w:val="${color}"/><w:sz w:val="${size * 2}"/><w:szCs w:val="${size * 2}"/></w:rPr></w:style>`;
  };
  return `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:styles xmlns:w="${W_NS}">${style("Normal", "Normal", 11, "000000", false, 0, 120, 264)}${style("Title", "Title", 22, "0B2545", true, 0, 120, 300)}${style("Subtitle", "Subtitle", 11, "44546A", false, 0, 240, 264)}${style("Heading1", "Heading 1", 16, "2E74B5", true, 320, 160, 280)}${style("Heading2", "Heading 2", 13, "2E74B5", true, 240, 120, 280)}${style("Heading3", "Heading 3", 12, "1F4D78", true, 160, 80, 280)}${style("ListParagraph", "List Paragraph", 11, "000000", false, 0, 80, 280)}${style("TableText", "Table Text", 9, "000000", false, 0, 0, 240)}${style("TableHeader", "Table Header", 9, "0B2545", true, 0, 0, 240)}</w:styles>`;
}

function documentXml(body) {
  return `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:document xmlns:w="${W_NS}" xmlns:r="${R_NS}"><w:body>${body}<w:sectPr><w:pgSz w:w="12240" w:h="15840"/><w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440" w:header="708" w:footer="708" w:gutter="0"/><w:cols w:space="720"/><w:docGrid w:linePitch="360"/></w:sectPr></w:body></w:document>`;
}

function numbering() {
  const abstractNums = usedNumbering
    .map(({ numId, kind }) => {
      const fmt = kind === "ordered" ? "decimal" : "bullet";
      const marker = kind === "ordered" ? "%1." : "•";
      return `<w:abstractNum w:abstractNumId="${numId}"><w:multiLevelType w:val="singleLevel"/><w:lvl w:ilvl="0"><w:start w:val="1"/><w:numFmt w:val="${fmt}"/><w:lvlText w:val="${marker}"/><w:pPr><w:tabs><w:tab w:val="num" w:pos="720"/></w:tabs><w:ind w:left="720" w:hanging="360"/></w:pPr></w:lvl></w:abstractNum>`;
    })
    .join("");
  const nums = usedNumbering
    .map(({ numId }) => `<w:num w:numId="${numId}"><w:abstractNumId w:val="${numId}"/></w:num>`)
    .join("");
  return `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:numbering xmlns:w="${W_NS}">${abstractNums}${nums}</w:numbering>`;
}

function files(markdown) {
  usedNumbering = [];
  const now = new Date().toISOString().replace(/\.\d{3}Z$/, "Z");
  const body = parseMarkdown(markdown);
  return {
    "[Content_Types].xml": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/><Default Extension="xml" ContentType="application/xml"/><Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/><Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/><Override PartName="/word/numbering.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml"/><Override PartName="/word/settings.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml"/><Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/><Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/></Types>`,
    "_rels/.rels": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"><Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/><Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/><Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/></Relationships>`,
    "word/_rels/document.xml.rels": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"><Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/><Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering" Target="numbering.xml"/><Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/></Relationships>`,
    "word/document.xml": documentXml(body),
    "word/styles.xml": styles(),
    "word/numbering.xml": numbering(),
    "word/settings.xml": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:settings xmlns:w="${W_NS}"><w:defaultTabStop w:val="720"/><w:characterSpacingControl w:val="doNotCompress"/></w:settings>`,
    "docProps/core.xml": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dcmitype="http://purl.org/dc/dcmitype/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><dc:title>校园招聘与就业信息管理系统实训报告</dc:title><dc:creator>Codex</dc:creator><cp:lastModifiedBy>Codex</cp:lastModifiedBy><dcterms:created xsi:type="dcterms:W3CDTF">${now}</dcterms:created><dcterms:modified xsi:type="dcterms:W3CDTF">${now}</dcterms:modified></cp:coreProperties>`,
    "docProps/app.xml": `<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes"><Application>Codex</Application><DocSecurity>0</DocSecurity><ScaleCrop>false</ScaleCrop></Properties>`,
  };
}

const crcTable = (() => {
  const table = [];
  for (let n = 0; n < 256; n++) {
    let c = n;
    for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
    table[n] = c >>> 0;
  }
  return table;
})();

function crc32(buffer) {
  let crc = 0xffffffff;
  for (const b of buffer) crc = crcTable[(crc ^ b) & 0xff] ^ (crc >>> 8);
  return (crc ^ 0xffffffff) >>> 0;
}

function dosDateTime(date = new Date()) {
  const time = (date.getHours() << 11) | (date.getMinutes() << 5) | Math.floor(date.getSeconds() / 2);
  const d = ((date.getFullYear() - 1980) << 9) | ((date.getMonth() + 1) << 5) | date.getDate();
  return { time, date: d };
}

function u16(n) {
  const b = Buffer.alloc(2);
  b.writeUInt16LE(n);
  return b;
}

function u32(n) {
  const b = Buffer.alloc(4);
  b.writeUInt32LE(n >>> 0);
  return b;
}

function makeZip(entries, outPath) {
  const locals = [];
  const centrals = [];
  let offset = 0;
  const { time, date } = dosDateTime();
  for (const [name, content] of Object.entries(entries)) {
    const nameBuf = Buffer.from(name);
    const raw = Buffer.from(content, "utf8");
    const compressed = zlib.deflateRawSync(raw);
    const crc = crc32(raw);
    const local = Buffer.concat([
      u32(0x04034b50), u16(20), u16(0), u16(8), u16(time), u16(date), u32(crc),
      u32(compressed.length), u32(raw.length), u16(nameBuf.length), u16(0), nameBuf, compressed,
    ]);
    locals.push(local);
    const central = Buffer.concat([
      u32(0x02014b50), u16(20), u16(20), u16(0), u16(8), u16(time), u16(date), u32(crc),
      u32(compressed.length), u32(raw.length), u16(nameBuf.length), u16(0), u16(0), u16(0), u16(0), u32(0), u32(offset), nameBuf,
    ]);
    centrals.push(central);
    offset += local.length;
  }
  const centralStart = offset;
  const central = Buffer.concat(centrals);
  const end = Buffer.concat([
    u32(0x06054b50), u16(0), u16(0), u16(centrals.length), u16(centrals.length),
    u32(central.length), u32(centralStart), u16(0),
  ]);
  fs.mkdirSync(path.dirname(outPath), { recursive: true });
  fs.writeFileSync(outPath, Buffer.concat([...locals, central, end]));
}

if (process.argv.length !== 4) {
  console.error("usage: markdown_report_to_docx.js input.md output.docx");
  process.exit(2);
}

const markdown = fs.readFileSync(process.argv[2], "utf8");
makeZip(files(markdown), process.argv[3]);
