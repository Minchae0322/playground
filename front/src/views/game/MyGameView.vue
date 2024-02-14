<template>
  <div class="page-title-container">
    <h2>我的比赛</h2>
    <p>Check out my matches</p>
    <div class="page-title-container-border"></div>
  </div>

  <div class="date-selector">
    <button @click="selectThisMonth">This Month</button>
    <input type="month" v-model="selectedMonth" @change="monthChanged">
  </div>

  <div class="content">
    <div class="section">
      <div class="game-list-title">过去比赛</div>
      <div class="game" v-for="game in pastGames" :key="game.id">
        <div class="game-info">
          <div class="game-title">{{ game.gameName }}
            <div class="game-type">{{ game.gameType }}</div>
          </div>
          <div class="game-date">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                 class="time-icon">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            {{ formatDate(game.gameStart) }}
          </div>
          <div class="game-description">
            <div class="host"><img src="../../assets/icon-host.png">{{ game.hostName }}</div>
            <div class="host"><img src="../../assets/icon-location.png">{{ game.location }}</div>
          </div>
        </div>
        <div class="game-time">{{ formatTime(game.gameStart) }} - {{
            formatEndTime(game.gameStart, game.runningTime)
          }}
        </div>
      </div>
    </div>

    <div class="section">
      <div class="game-list-title">未来比赛</div>
      <div v-for="game in upcomingGames" :key="game.gameId">
        <router-link class="game"
                     :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-info">
            <div class="game-title">{{ game.gameName }}
              <div class="game-type">{{ game.gameType }}</div>
            </div>
            <div class="game-date">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="1" stroke-linecap="round" stroke-linejoin="round"
                   class="time-icon">
                <circle cx="12" cy="12" r="10"></circle>
                <polyline points="12 6 12 12 16 14"></polyline>
              </svg>
              {{ formatDate(game.gameStart) }}
            </div>

            <div class="game-description">
              <div class="host"><img src="../../assets/icon-host.png">{{ game.hostName }}</div>
              <div class="host"><img src="../../assets/icon-location.png">{{ game.location }}</div>
            </div>
          </div>

          <div class="game-time">{{ formatTime(game.gameStart) }} - {{
              formatEndTime(game.gameStart, game.runningTime)
            }}
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from "@/assets/img.png";
import {getCurrentInstance, onMounted, ref} from "vue";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

const pastGames = ref([]);
const upcomingGames = ref([{
  playgroundId: 1,
  gameId: 1,
}]);
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
  const now = new Date();
  await getMyGame(now.getFullYear(), now.getMonth() + 1)
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
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
}

function formatEndTime(startTime, runningTime) {
  const startDate = new Date(startTime);
  const endDate = new Date(startDate.getTime() + runningTime * 60000); // runningTime을 분 단위로 가정합니다.
  return endDate.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
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
      headers: {'RefreshToken': refreshToken}
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
.date-selector {
  display: flex; /* 요소들을 가로로 정렬합니다 */
  align-items: center; /* 요소들을 세로 중앙에 배치합니다 */
  gap: 10px; /* 버튼과 입력 필드 사이의 간격을 설정합니다 */
  padding: 10px; /* date-selector 주변의 패딩을 설정합니다 */
  background-color: var(--background-color-gray); /* 배경색을 설정합니다 */
}

.date-selector button {
  padding: 10px 20px; /* 버튼 내부의 상하, 좌우 패딩을 설정합니다 */
  background-color: var(--primary-color); /* 버튼의 배경색을 설정합니다 */
  color: white; /* 버튼의 텍스트 색상을 설정합니다 */
  border: none; /* 버튼의 테두리를 제거합니다 */
  margin-left: 10px;
  font-family: MiSans-Medium, sans-serif;
  border-radius: 5px; /* 버튼의 모서리를 둥글게 처리합니다 */
  cursor: pointer; /* 마우스를 올렸을 때 커서가 손가락 모양으로 변경되도록 설정합니다 */
  font-size: 14px; /* 텍스트 크기를 설정합니다 */
}

.date-selector button:hover {
  background-color: var(--secondary-color); /* 마우스를 버튼 위에 올렸을 때의 배경색을 변경합니다 */
}

.date-selector input[type="month"] {
  padding: 10px; /* 입력 필드 내부의 패딩을 설정합니다 */
  border: 1px solid #ccc; /* 입력 필드의 테두리를 설정합니다 */
  border-radius: 5px; /* 입력 필드의 모서리를 둥글게 처리합니다 */
  font-size: 14px; /* 텍스트 크기를 설정합니다 */
  font-family: MiSans-Medium, sans-serif;

}

.date-selector input[type="month"]:focus {
  outline: none; /* 입력 필드에 포커스 되었을 때의 기본 테두리를 제거합니다 */
  border-color: #425fff; /* 입력 필드에 포커스 되었을 때의 테두리 색상을 변경합니다 */
}

.game-list-title {
  font-family: MiSans-Semibold, sans-serif;
  color: #001a56;
  font-size: 21px;
  margin-bottom: 10px;
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
  flex-wrap: wrap; /* 내용이 넘치면 다음 줄로 넘깁니다 */
}

.section {
  margin-bottom: 10px;
  background-color: #ffffff;
  padding: 20px;
  width: 48%;
  min-width: 530px;
  overflow: auto; /* 내용이 넘칠 경우 스크롤바 표시 */
  height: auto;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}


.game {
  display: flex;
  margin-bottom: 20px;
  justify-content: space-between;
  align-items: center;
  border-radius: 8px;
  padding: 15px 25px;
  line-height: 1.2;
  border: 1px solid #e6e6e6;
}

.game:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 강조 */
  transform: translateY(-5px); /* 호버 시 카드가 약간 위로 올라가는 효과 */
}

.game-info {
  display: flex;

  flex-direction: column;
}

.game-title {
  font-size: 22px;
  display: flex;
  font-family: MiSans-Semibold, sans-serif;
  color: black;

}

.game-date {
  display: flex;
  font-size: 16px;
  margin-bottom: 10px;
  color: var(--text-hint-dark);
  font-family: MiSans-Normal, sans-serif;
}

.game-date svg {
  width: 14px;
  color: black;
  margin-right: 5px;
}

.game-description {
  display: flex;
  gap: 10px;
}

.host {
  font-size: 14px;
  display: flex;
  color: #0064cc;
  font-family: MiSans-Medium, sans-serif;

}

.host img {
  width: 15px;
  height: 15px;
  margin: auto 2px auto 0;

}

.game-type {
  color: #eab800;

  font-size: 19px;
  margin: auto 10px;
}


.game-time {
  font-size: 16px;
  color: var(--text-hint-dark);
  font-family: MiSans-Normal, sans-serif;
}
</style>