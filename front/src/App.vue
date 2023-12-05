<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <body>
  <header>
    <div class="header_menu">
      <div class="menu_small">

        <div class="menu_logo">

          <a href="/home"> MEME </a>
        </div>

        <ul class="menu_nav">
          <li>
            <RouterLink to="/login">인기글</RouterLink>
          </li>

        </ul>
      </div>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
  </body>
</template>

<style scoped>
body {
  background: white;
  min-width: 95vw;
  display: flow;
  justify-content: center;
  align-items: center;
  margin-top: 10px;

}
</style>
