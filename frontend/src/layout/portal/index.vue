<template>
  <div class="portal-wrapper">
    <PortalHeader />
    <main class="portal-main">
      <router-view v-slot="{ Component }">
        <transition
          @before-enter="onBeforeEnter"
          @enter="onEnter"
          @leave="onLeave"
          mode="out-in"
        >
          <component :is="Component" :key="$route.path" />
        </transition>
      </router-view>
    </main>
    <PortalFooter />
  </div>
</template>

<script setup>
import PortalHeader from './PortalHeader.vue'
import PortalFooter from './PortalFooter.vue'
import { animate } from 'animejs'

function onBeforeEnter(el) {
  el.style.opacity = 0
}

function onEnter(el, done) {
  animate(el, {
    opacity: [0, 1],
    translateY: [12, 0],
    duration: 320,
    ease: 'out(2)',
    onComplete: done
  })
}

function onLeave(el, done) {
  animate(el, {
    opacity: [1, 0],
    translateY: [0, -8],
    duration: 200,
    ease: 'in(2)',
    onComplete: done
  })
}
</script>

<style lang="scss">
@use '../../assets/styles/portal.scss' as *;

.portal-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--portal-bg);
}

.portal-main {
  flex: 1;
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
  padding: 24px 24px 48px;
}

@media (max-width: 768px) {
  .portal-main {
    padding: 16px 12px 32px;
  }
}
</style>
