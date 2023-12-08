<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>






  <body>
  <main>
    <div class="margin-top"></div>
    <RouterView class="" />
  </main>
  </body>
</template>

<style scoped>

body {
  background: white;
  min-width: 97vw;
  display: flow;
  justify-content: center;
  align-items: center;

}

.margin-top {
  padding-bottom: 50px;
}
</style>
