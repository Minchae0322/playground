<template>
  <div id="gameView-container">
    <component :is="currentView" :game="selectedGame" key="selectedGame.gameId"></component>
    <div class="game-info-container">
      <div v-if="currentGame">
        <div><strong>Host</strong> {{ currentGame.hostName }}</div>
        <div><strong>StartTime</strong> {{ currentGame.gameStart }}</div>
        <div><strong>Running Time</strong> {{ currentGame.time }}</div>
      </div>
      <div v-else>
        <div>No current game in progress</div>
      </div>
    </div>

    <div class="upcoming-games">
      <h3>Upcoming Games</h3>
      <ul v-if="upcomingGames">
        <li v-for="(game,gameId) in upcomingGames" :key="gameId" @click="handleGameClick(game)">
          <div >
            <div  class="game-card">
              <h2>{{ game.gameName }}</h2>
              <div class="host-info">
                <img :src="game.hostProfileImg || defaultImage">
                <h3>Host: {{ game.hostName }}</h3>
              </div>
              <p class="game-time">Time: {{ game.gameStart }}</p>
              <p class="running-time">Running Time: {{ game.runningTime }}</p>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <button class="join-button" @click="openGameBuilder">Join Game</button>
    <GameBuilderModal v-if="isGameBuilderModalOpen" :some-data="data" @closeGameBuilder=closeModal></GameBuilderModal>
  </div>

</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import GameBuilderModal from './GameBuilderView.vue';
import {useRouter} from "vue-router";
import { defineEmits } from 'vue';


const data = ref('이것은 부모로부터 온 데이터입니다.');
const isGameBuilderModalOpen = ref(false);
const currentGame = ref('')
const upcomingGames = ref([{

}]);
const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const emits = defineEmits(['gameSelected']);

onMounted(async () => {
  // Check if the initial page number is provided in the route query
  getInProgressGame();
  await getUpcomingGames();
});

function handleGameClick(game) {
  // 게임이 클릭되었을 때 부모 컴포넌트에 알림
  emits('gameSelected', game);
}



const openGameBuilder = function () {
  isGameBuilderModalOpen.value = !isGameBuilderModalOpen.value;
};


const closeModal = () => {
  isGameBuilderModalOpen.value = false;
  getUpcomingGames()
};



const getInProgressGame = async function () {
  await validateAccessToken()

  await axios.get(`${apiBaseUrl}/playground/2/current`,
        {  headers: {
            'Authorization': getAccessToken()
          }}
    ).then(response => {
      if (response.status === 200) {
        currentGame.value = response.data;
      }
    });
}

const getUpcomingGames = async () => {
  await validateAccessToken()
   await axios.get(`${apiBaseUrl}/playground/2/upComing`,
        {  headers: {
            'Authorization': getAccessToken()
          }}
    ).then(response => {
      upcomingGames.value = response.data.map(game => ({
        ...game,
        hostProfileImg: game.hostProfileImg ? `data:image/jpeg;base64,${game.hostProfileImg}` : defaultImage,
        onClick: () => showGameInfo(game.gameId)
      }));
    });

};

const validateAccessToken = async function () {
  const accessToken = getAccessToken();
  if (!accessToken) {
    redirectToLogin()
    return;
  }

  try {
    await axios.get(`${apiBaseUrl}/token/valid`, {
      headers: {'Authorization': accessToken},
    });
  } catch (error) {
    await updateAccessToken();
  }
};

const getAccessToken = function () {
  return localStorage.getItem("accessToken");
};

const updateAccessToken = async function () {
  const refreshToken = localStorage.getItem("refreshToken");
  if (!refreshToken) return redirectToLogin();

  try {
    const response = await axios.get(`${apiBaseUrl}/token/refresh`, {
      headers: { 'RefreshToken': refreshToken }
    });
    if (response.status === 200) {
      const newAccessToken = response.headers['authorization'];
      localStorage.setItem('accessToken', newAccessToken);
      return newAccessToken;
    }
  } catch (error) {
    redirectToLogin();
  }
};

const redirectToLogin = function () {
  router.push("/login");
};


</script>

<style scoped>

body {
  color: #333333;
}

div {
  color: #333333;
}
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




.game-info-container {
  border: 1px solid #ddd; /* Add a border */
  padding: 15px; /* Add some padding inside the card */
  margin-top: 20px; /* Add some space above the card */
  background-color: #fff; /* Set a background color */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Add a subtle shadow */
  border-radius: 8px; /* Optional: rounded corners */
  cursor: pointer; /* Indicates it's clickable */
  transition: box-shadow 0.3s; /* Smooth transition for hover effect */
}

.game-info-container:hover {
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
  background-color: #fff; /* 배경색을 흰색으로 설정 */
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.upcoming-games h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 1.25rem; /* 크기를 조정하여 가독성을 높임 */
}

.upcoming-games ul {
  list-style: none;
  padding: 0;
}

.upcoming-games li {
  margin-bottom: 15px; /* 각 게임 카드 사이의 간격을 늘림 */
  color: #555;
}



.host-info {
  display: flex; /* Flexbox를 이용한 가로 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  margin-bottom: 0.5rem; /* 아래쪽 여백 */
}

.host-info img {
  border-radius: 50%;
  background-color: #ddd; /* 아이콘 배경색 설정 */
  display: inline-block;
  width: 25px; /* 아이콘 크기 */
  height: 25px;
  margin-right: 10px; /* 아이콘과 텍스트 사이의 간격 */
}

.host-info h3 {
  font-size: 1rem; /* 호스트 이름의 글씨 크기 */
  color: #333; /* 호스트 이름의 글씨 색상 */
  margin: 0; /* 여백 초기화 */
}
.game-card {
  border: 1px solid #ddd;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05); /* 그림자를 더 부드럽게 조정 */
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.game-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 강조 */
}

.game-card h2 {
  color: #000;
  font-size: 24px;
  text-decoration: none;
  margin-bottom: 10px;
  font-family: "MS UI Gothic";
}


.game-card h3 {
  font-size: 1rem;
  color: #000;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;

}

.game-card p {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif
}

/* 아이콘 스타일링 */
.game-card img {
  border-radius: 50%;
  background-color: #ddd; /* 아이콘 배경색 설정 */
  display: inline-block;
  width: 40px; /* 아이콘 크기 조정 */
  height: 40px;
  margin-right: 8px; /* 아이콘과 텍스트 사이의 간격 조정 */
  vertical-align: middle; /* 수직 정렬 */
}

/* 시간과 실행 시간 정보 스타일링 */
.game-card .game-time {
  font-size: 0.875rem;
  color: #666;
  margin-top: 0.5rem;
}

.game-card .running-time {
  font-size: 0.875rem;
  color: #666;
  margin-top: 0.25rem;
}

/* 링크 스타일링 제거 */
.game-card a {
  text-decoration: none;
  color: inherit;
}

a {

  text-decoration: none;
}
</style>