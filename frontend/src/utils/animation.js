import { animate } from 'animejs'

// 按钮点击波纹动画
export function buttonRipple(e) {
  const btn = e.currentTarget
  const rect = btn.getBoundingClientRect()
  const ripple = document.createElement('span')
  ripple.style.cssText = `
    position: absolute; border-radius: 50%; background: rgba(255,255,255,0.4);
    width: 0; height: 0; left: ${e.clientX - rect.left}px; top: ${e.clientY - rect.top}px;
    transform: translate(-50%, -50%); pointer-events: none;
  `
  btn.style.position = 'relative'
  btn.style.overflow = 'hidden'
  btn.appendChild(ripple)

  animate(ripple, {
    width: [0, rect.width * 2.5],
    height: [0, rect.width * 2.5],
    opacity: [0.6, 0],
    duration: 600,
    ease: 'out(3)',
    onComplete: () => ripple.remove()
  })
}

// 按钮悬停缩放动画
export function buttonHover(el) {
  el.addEventListener('mouseenter', () => {
    animate(el, { scale: 1.04, duration: 200, ease: 'out(2)' })
  })
  el.addEventListener('mouseleave', () => {
    animate(el, { scale: 1, duration: 200, ease: 'out(2)' })
  })
}

// 卡片入场动画（交错）
export function staggerCards(selector, container) {
  const targets = container ? container.querySelectorAll(selector) : document.querySelectorAll(selector)
  animate(targets, {
    opacity: [0, 1],
    translateY: [24, 0],
    duration: 400,
    delay: (_, i) => i * 60,
    ease: 'out(2)'
  })
}

// 数字跳动动画
export function countUp(el, endValue, duration = 800) {
  const obj = { val: 0 }
  animate(obj, {
    val: endValue,
    duration,
    ease: 'out(2)',
    onUpdate: () => {
      el.textContent = Math.round(obj.val)
    }
  })
}

// 页面进入动画
export function pageEnter(el) {
  animate(el, {
    opacity: [0, 1],
    translateY: [16, 0],
    duration: 350,
    ease: 'out(2)'
  })
}

// 淡入
export function fadeIn(el, duration = 300) {
  animate(el, { opacity: [0, 1], duration, ease: 'out(2)' })
}

// 淡出
export function fadeOut(el, duration = 300) {
  return animate(el, { opacity: [1, 0], duration, ease: 'in(2)' })
}
