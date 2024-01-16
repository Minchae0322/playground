<template>
  <div class="navbar">
    <a href="#">My Games</a>
  </div>



  <div class="content">
    <div class="section ">
      <h2>Upcoming Games</h2>
      <div class="game" v-for="game in upcomingGames" :key="game.id">
        <div class="game-info">
          <div class="game-title">{{ formatDate(game.gameStart) }}</div>
          <div class="host">Hosted by {{ game.hostName }}</div>
        </div>
        <div class="game-time">{{ formatTime(game.gameStart) }} - {{ formatEndTime(game.gameStart, game.runningTime) }}</div>
        <div class="game-actions">
          <button @click=deleteGame(game.gameId) class="delete">Delete</button>
        </div>
      </div>

    </div>


    <div class="date-selector">
      <button @click="selectThisMonth">This Month</button>
      <input type="month" v-model="selectedMonth" @change="monthChanged">
    </div>
    <div class="section ">
      <h2>Past Games</h2>
      <div class="game" v-for="game in pastGames" :key="game.id">
        <div class="game-info">
          <div class="game-title">{{ formatDate(game.gameStart) }}</div>
          <div class="host">Hosted by {{ game.hostName }}</div>
        </div>
        <div class="game-time">{{ formatTime(game.gameStart) }} - {{ formatEndTime(game.gameStart, game.runningTime) }}</div>
        <div class="game-actions">
          <button class="submit">Submit</button>
          <button class="delete">Delete</button>
        </div>
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

onMounted(async () => {
  selectThisMonth();
});

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




const getMyGame = async (year, month) => {
  await validateAccessToken();

  pastGames.value = [];
  upcomingGames.value = [];

  try {
    const response = await axios.get(`${apiBaseUrl}/user/game/host`, {
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


const deleteGame = async (gameId) => {
  const isConfirm = confirm("정말 삭제하시겠습니까?");
  if (!isConfirm) {
    return;
  }
  await validateAccessToken();

  try {
    const response = await axios.delete(`${apiBaseUrl}/user/game/${gameId}/delete`, {
      headers: {
        'Authorization': getAccessToken(),
      }
    });
    alert("삭제되었습니다.")
    await monthChanged()
  } catch (error) {
    alert(error.response.data.message)
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
  justify-content: center; /* 가운데 정렬 */
  align-items: start; /* 상단 정렬 */
  flex-direction: column;
  padding: 20px;
}
.section {
  background-color: #ffffff;
  padding: 20px;
  width: 100%;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
h2 {
  margin: 0 0 20px 0;
}
.game {
  background: #FFF;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.game-header h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

.game-details p {
  font-size: 16px;
  color: #666;
  margin-bottom: 5px;
}

.game-input input[type='text'] {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.game-actions {
  display: flex;
  justify-content: space-between;
}

.game-actions button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.game-actions .submit {
  background-color: #5cb85c;
  color: white;
}

.game-actions .delete {
  background-color: var(--white);
  color:black;
  border: 1px solid black;
  display: flex;
  justify-content: space-between;
  width: 100%;
  text-align: center;
  align-items: center;
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


</style>
