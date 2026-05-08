<template>
  <div class="stat-card" :class="gradientClass">
    <div class="stat-card__content">
      <div class="stat-card__value">{{ animatedValue }}</div>
      <div class="stat-card__label">{{ label }}</div>
    </div>
    <div class="stat-card__icon">
      <el-icon :size="32"><component :is="icon" /></el-icon>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  value: { type: [Number, String], default: 0 },
  label: { type: String, default: '' },
  icon: { type: [Object, String], default: '' },
  color: { type: String, default: 'blue' }
})

const gradientClass = computed(() => `stat-gradient-${props.color}`)

const animatedValue = ref(0)

watch(() => props.value, (newVal) => {
  const target = Number(newVal) || 0
  const duration = 600
  const start = animatedValue.value
  const diff = target - start
  if (diff === 0) return
  const startTime = performance.now()
  function step(currentTime) {
    const elapsed = currentTime - startTime
    const progress = Math.min(elapsed / duration, 1)
    const ease = 1 - Math.pow(1 - progress, 3)
    animatedValue.value = Math.round(start + diff * ease)
    if (progress < 1) requestAnimationFrame(step)
  }
  requestAnimationFrame(step)
}, { immediate: true })
</script>

<style lang="scss" scoped>
.stat-card {
  border-radius: 12px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -30%;
    right: -10%;
    width: 120px;
    height: 120px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
  }

  &__content {
    position: relative;
    z-index: 1;
  }

  &__value {
    font-size: 32px;
    font-weight: 800;
    line-height: 1.2;
    letter-spacing: -1px;
  }

  &__label {
    font-size: 13px;
    font-weight: 500;
    opacity: 0.9;
    margin-top: 4px;
  }

  &__icon {
    position: relative;
    z-index: 1;
    opacity: 0.7;
  }
}
</style>
