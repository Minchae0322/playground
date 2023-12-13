<template>


  <body>

  <div class="image-container">
    <!-- Your image goes here -->
    <img src="../assets/soccer.png" alt="Game Image">
    <div class="overlay">
      <h1>Game Title</h1>
      <p>Game Description</p>
      <p>Start Time: 10:00 AM</p>
    </div>
  </div>

  </body>
</template>

<style>
.image-container {
  position: relative;
  width: 100%;
  max-width: 400px; /* You can set this to the width of your image */
}

.image-container img {
  width: 100%;
  height: auto;
  display: block;
}

.overlay {
  position: absolute;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
  color: white;
  width: 100%;
  padding: 10px;
  text-align: center;
}
</style>