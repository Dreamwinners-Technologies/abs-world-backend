<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style lang="scss">
#app {
  font-family: 'Fredoka', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

  html, body {
        height: 100vh;
        height: calc(var(--vh, 1vh) * 100);
        font-family: 'Fredoka', sans-serif;
        margin: 0;
      }

      .container {
        margin: 0 auto;
        margin-top: 180px;
        padding: 0 10px;
      }

      .main {
        text-align: center;
      }

      .image {
        text-align: center;
      }
</style>
