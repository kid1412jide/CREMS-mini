<template>
  <footer class="portal-footer">
    <div class="portal-footer__inner">
      <div class="footer-left">
        <span class="footer-brand">CREMS</span>
        <span class="footer-sep">-</span>
        <span class="footer-desc">校园招聘与就业信息管理系统</span>
      </div>
      <div class="footer-right">
        <span class="footer-copy">Copyright &copy; 2026 CREMS. All Rights Reserved.</span>
      </div>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
.portal-footer {
  background: #fff;
  border-top: 1px solid #f0f0f0;
  padding: 20px 0;

  &__inner {
    max-width: 1280px;
    margin: 0 auto;
    padding: 0 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 8px;
  }
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.footer-brand {
  font-weight: 700;
  font-size: 14px;
  color: #303133;
}

.footer-sep {
  color: #c0c4cc;
}

.footer-desc {
  font-size: 13px;
  color: #909399;
}

.footer-copy {
  font-size: 12px;
  color: #c0c4cc;
}

@media (max-width: 768px) {
  .portal-footer__inner {
    flex-direction: column;
    text-align: center;
    padding: 0 16px;
  }
}
</style>
