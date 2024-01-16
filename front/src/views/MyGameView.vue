<template>
  <div class="navbar">
    <a href="#">My Games</a>
  </div>

  <div class="date-selector">
    <button @click="selectThisMonth">This Month</button>
    <input type="month" v-model="selectedMonth" @change="monthChanged">
  </div>

  <div class="content">
    <div class="section past-games">
      <h2>Past Games</h2>
      <div class="game" v-for="game in pastGames" :key="game.id">
        <div class="game-info">
          <div class="game-title">{{ formatDate(game.gameStart) }}</div>
          <div class="host">Hosted by {{ game.hostName }}</div>
          <div class="host">Hosted by {{ game.subTeamName }}</div>
        </div>
        <div class="game-time">{{ formatTime(game.gameStart) }} - {{ formatEndTime(game.gameStart, game.runningTime) }}</div>
      </div>
    </div>

    <div class="section upcoming-games">
      <h2>Upcoming Games</h2>
      <div class="game" v-for="game in upcomingGames" :key="game.id">
        <div class="game-info">
          <div class="game-title">{{ formatDate(game.gameStart) }}</div>
          <div class="host">Hosted by {{ game.hostName }}</div>
          <div class="host">Team : {{ game.subTeamName }} </div>
        </div>
        <div class="game-time">{{ formatTime(game.gameStart) }} - {{ formatEndTime(game.gameStart, game.runningTime) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from "@/assets/img.png";
import {onMounted, ref} from "vue";

const apiBaseUrl = "http://localhost:8080";

const pastGames = ref([]);
const upcomingGames = ref([]);
const selectedMonth = ref(new Date().toISOString().substring(0, 7));



const monthChanged = async () => {
  const [year, month] = selectedMonth.value.split('-');
  await getMyGame(parseInt(year), parseInt(month));
};

const selectThisMonth = () => {
  const now = new Date();
  console.log(now)
  selectedMonth.value = now.toISOString().substring(0, 7);
  monthChanged(); // 이 함수를 호출하여 선택된 달에 대한 데이터를 가져옵니다.
};


onMounted(async () => {
  await getMyGame(2024, 1)
});

const getMyGame = async (year, month) => {
  await validateAccessToken();

  pastGames.value = [];
  upcomingGames.value = [];

  try {
    const response = await axios.get(`${apiBaseUrl}/user/game/${year}/${month}`, {
      headers: {
        'Authorization': getAccessToken(),
      }
    });

    const currentTime = new Date();

    // response.data의 각 아이템에 대해 반복
    response.data.forEach(game => {
      // game.startTime이 현재 시간 이전이라면 pastGames 배열에 추가
      // game.startTime이 현재 시간 이후라면 upcomingGames 배열에 추가
      if (new Date(game.localDateStartTime) < currentTime) {
        pastGames.value.push(game);
      } else {
        upcomingGames.value.push(game);
      }
    });

  } catch (error) {
    console.error('게임 정보를 가져오는데 실패했습니다.', error.response.data.message);
  }
};

function formatDate(dateTime) {
  const date = new Date(dateTime);
  return date.toLocaleDateString();
}

function formatTime(dateTime) {
  const date = new Date(dateTime);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function formatEndTime(startTime, runningTime) {
  const startDate = new Date(startTime);
  const endDate = new Date(startDate.getTime() + runningTime * 60000); // runningTime을 분 단위로 가정합니다.
  return endDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

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
  background-color: white;
}
.navbar {
  background-color: #f2f2f2;
  padding: 15px;
  text-align: left;
}
.navbar a {
  color: black;
  text-decoration: none;
  font-size: 24px;
}
.content {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}
.section {
  background-color: #ffffff;
  padding: 20px;
  width: 48%;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
h2 {
  margin: 0 0 20px 0;
}
.game {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e6e6e6;
}
.game:last-child {
  border-bottom: none;
}
.game-info {
  display: flex;
  flex-direction: column;
}
.game-title {
  font-size: 18px;
  color: #333;
}
.host {
  font-size: 14px;
  color: #666;
}
.game-time {
  font-size: 14px;
  color: #666;
}.navbar {
   background-color: #f2f2f2;
   padding: 15px;
   text-align: left;
 }
.navbar a {
  color: black;
  text-decoration: none;
  font-size: 24px;
}
.content {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}
.section {
  background-color: #ffffff;
  padding: 20px;
  width: 48%;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
h2 {
  margin: 0 0 20px 0;
}
.game {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e6e6e6;
}
.game:last-child {
  border-bottom: none;
}
.game-info {
  display: flex;
  flex-direction: column;
}
.game-title {
  font-size: 18px;
  color: #333;
}
.host {
  font-size: 14px;
  color: #666;
}
.game-time {
  font-size: 14px;
  color: #666;
}.navbar {
   background-color: #f2f2f2;
   padding: 15px;
   text-align: left;
 }
.navbar a {
  color: black;
  text-decoration: none;
  font-size: 24px;
}
.content {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}
.section {
  background-color: #ffffff;
  padding: 20px;
  width: 48%;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
h2 {
  margin: 0 0 20px 0;
}
.game {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e6e6e6;
}
.game:last-child {
  border-bottom: none;
}
.game-info {
  display: flex;
  flex-direction: column;
}
.game-title {
  font-size: 18px;
  color: #333;
}
.host {
  font-size: 14px;
  color: #666;
}
.game-time {
  font-size: 14px;
  color: #666;
}
</style>