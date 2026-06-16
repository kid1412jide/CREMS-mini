import { animate } from 'animejs'

// 按钮悬停 + 点击动画指令
export const vAnimeButton = {
  mounted(el) {
    // 保存事件函数到元素实例，卸载时才能准确移除监听，避免页面切换后残留。
    el.style.position = el.style.position || 'relative'
    el.style.overflow = el.style.overflow || 'hidden'

    el._animeEnter = () => {
      animate(el, { scale: 1.05, duration: 180, ease: 'out(2)' })
    }
    el._animeLeave = () => {
      animate(el, { scale: 1, duration: 180, ease: 'out(2)' })
    }
    el._animeDown = () => {
      animate(el, { scale: 0.96, duration: 100, ease: 'out(3)' })
    }
    el._animeUp = () => {
      animate(el, { scale: 1.05, duration: 120, ease: 'out(2)' })
    }

    el.addEventListener('mouseenter', el._animeEnter)
    el.addEventListener('mouseleave', el._animeLeave)
    el.addEventListener('mousedown', el._animeDown)
    el.addEventListener('mouseup', el._animeUp)
  },
  unmounted(el) {
    el.removeEventListener('mouseenter', el._animeEnter)
    el.removeEventListener('mouseleave', el._animeLeave)
    el.removeEventListener('mousedown', el._animeDown)
    el.removeEventListener('mouseup', el._animeUp)
  }
}

// 卡片悬停动画指令
export const vAnimeCard = {
  mounted(el) {
    el._animeEnter = () => {
      animate(el, { translateY: -4, boxShadow: '0 8px 24px rgba(0,0,0,0.1)', duration: 250, ease: 'out(2)' })
    }
    el._animeLeave = () => {
      animate(el, { translateY: 0, boxShadow: '0 1px 3px rgba(0,0,0,0.04)', duration: 250, ease: 'out(2)' })
    }
    el.addEventListener('mouseenter', el._animeEnter)
    el.addEventListener('mouseleave', el._animeLeave)
  },
  unmounted(el) {
    el.removeEventListener('mouseenter', el._animeEnter)
    el.removeEventListener('mouseleave', el._animeLeave)
  }
}

// 列表项交错入场指令
export const vAnimeStagger = {
  mounted(el, binding) {
    // binding.value 控制每一项延迟时间，列表页可按索引形成依次入场效果。
    const delay = binding.value ?? 0
    el.style.opacity = '0'
    animate(el, {
      opacity: [0, 1],
      translateY: [20, 0],
      duration: 400,
      delay,
      ease: 'out(2)'
    })
  }
}
