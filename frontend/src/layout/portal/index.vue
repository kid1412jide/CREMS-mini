<template>
  <div class="portal-wrapper">
    <PortalHeader />
    <main class="portal-main">
      <router-view v-slot="{ Component }">
        <transition name="portal-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    <PortalFooter />
  </div>
</template>

<script setup>
import PortalHeader from './PortalHeader.vue'
import PortalFooter from './PortalFooter.vue'
</script>

<style lang="scss">
@import '@/assets/styles/portal.scss';

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

.portal-fade-enter-active,
.portal-fade-leave-active {
  transition: opacity 0.2s ease;
}

.portal-fade-enter-from,
.portal-fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .portal-main {
    padding: 16px 12px 32px;
  }
}
</style>
