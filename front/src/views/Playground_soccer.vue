<template>
  <div class="game-container">
    <div class="game-image">
      <!-- Your image goes here -->
      <img src="../assets/soccer.png" alt="Game Image">
      <div class="overlay">
        <h1>Game Title</h1>
        <p>Game Description</p>
        <p>Start Time: 10:00 AM</p>
      </div>
    </div>
    <div class="game-info">
      <div v-if="currentGame">
        <div><strong>Host</strong> {{ currentGame.hostName }}</div>
        <div><strong>StartTime</strong> {{ currentGame.gameStart }}</div>
        <div><strong>Running Time</strong> {{ currentGame.time }}</div>
      </div>
      <div v-else>
        <strong>No current game in progress</strong>
      </div>
    </div>

    <div class="upcoming-games">
      <h3>Upcoming Games</h3>
      <ul>
        <li v-for="(game,index) in upcomingGames" :key="index">
          <div class="game-card">
            <div><strong>Host:</strong> {{ game.hostName }}</div>
            <div><strong>Start Time:</strong> {{ game.gameStart }}</div>
            <div><strong>Running Time:</strong> {{ game.time }}</div>
          </div>
        </li>
      </ul>
    </div>
    <button class="join-button">Join Game</button>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";

const currentGame = ref('')
const upcomingGames = ref([])


onMounted(() => {
  // Check if the initial page number is provided in the route query
  getInProgressGame();
  getUpcomingGames();
});
const getInProgressGame = function () {

  const accessToken = localStorage.getItem("accessToken");
  if(accessToken) {
    axios.get(`$apiBaseUrlplayground/1/current`,
        {  headers: {
            'Authorization': accessToken
          }}
    ).then(response => {
      if (response.status === 200) {
        currentGame.value = response.data;
      }
    });
  }

};

const getUpcomingGames = function () {
  const accessToken = localStorage.getItem("accessToken");
  if(accessToken) {
    axios.get(`${apiBaseUrl}playground/1/upComing`,
        {  headers: {
            'Authorization': accessToken
          }}
    ).then(response => {
      upcomingGames.value = response.data
    });
  }

};

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

.game-image {
  position: relative;
  width: 100%;
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

.overlay {
  position: absolute;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
  color: white;
  width: 30%;
  padding: 10px;
  text-align: center;
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

.game-card {
  border: 1px solid #ddd;
  padding: 15px;
  margin-bottom: 10px; /* 각 카드 간의 간격 */
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.game-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>