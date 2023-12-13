<template>
  <div class="game-container">
    <div class="game-image">
      <img src="../assets/soccer.png" alt="Game Image"/>
    </div>
    <div class="game-header">
      <h2>Game Title</h2>
      <p>Game Description</p>
    </div>
    <div class="game-info" @click="showCurrentGame">
      <div><strong>Current Game</strong></div>
      <div><strong>Start Time:</strong> 10:00 AM</div>
      <div><strong>Duration:</strong> 2 hours</div>
      <div><strong>Additional Info:</strong> All players must be above 16</div>
    </div>

    <div class="upcoming-games">
      <h3>Upcoming Games</h3>
      <ul>
        <li v-for="game in upcomingGames" :key="game.id">
          <strong>{{ game.title }}</strong> - {{ game.startTime }}
        </li>
      </ul>
    </div>
    <button class="join-button">Join Game</button>
  </div>
</template>

<script>
export default {
  name: 'GameComponent'
  // You can include data, props, methods, etc., here as needed.
}
</script>

<style scoped>
.game-container {
  max-width: 70vw; /* Adjust the maximum width to your preference */
  width: 75vw; /* This will make the container take up 100% of its parent up to max-width */
  margin: auto;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 10px;
  background-color: #f9f9f9;
}

/* ... other styles ... */

.game-image img {
  width: 100%;
  height: auto; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 7; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}


.game-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.game-header p {
  color: #666;
  font-size: 16px;
}

.game-info {
  border: 1px solid #ddd; /* Add a border */
  padding: 15px; /* Add some padding inside the card */
  margin-top: 20px; /* Add some space above the card */
  background-color: #fff; /* Set a background color */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Add a subtle shadow */
  border-radius: 8px; /* Optional: rounded corners */
  cursor: pointer; /* Indicates it's clickable */
  transition: box-shadow 0.3s; /* Smooth transition for hover effect */
}

.game-info:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* Slightly larger shadow on hover */
}
.join-button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 20px;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.join-button:hover {
  background-color: #444;
}

.upcoming-games {
  margin-top: 20px;
  padding: 15px;
  background-color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.upcoming-games h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.upcoming-games ul {
  list-style: none;
  padding: 0;
}

.upcoming-games li {
  margin-bottom: 5px;
  color: #555;
}
</style>