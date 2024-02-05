<template>
  <div class="info-container">
    <div class="flex-box">
      <img src="../assets/icon-soccerball.png">
      <h2>足球</h2>
    </div>
    <div class="info-container-border"></div>
  </div>
  <div v-if="soccerGames.length === 0 || !soccerGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in soccerGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="upcoming-game-name">{{ game.gameName }} ({{ game.gameStart }})</div>
          <div class="upcoming-game-info-container">
            <div class="campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container" :to="{ name:'playgroundList', params: {sportsEvent : 'SOCCER'}}" >
    <button class="more-button">更多</button>
  </RouterLink>


  <div class="info-container">
    <div class="flex-box">
      <img src="../assets/icon-basketballBall.png">
      <h2>篮球</h2>
    </div>
    <div class="info-container-border"></div>
  </div>
  <div v-if="basketballGames.length === 0 || !basketballGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in basketballGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="upcoming-game-name">{{ game.gameName }} ({{ game.gameStart }})</div>
          <div class="upcoming-game-info-container">
            <div class="campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}" >
    <button class="more-button">更多</button>
  </RouterLink>

  <div class="info-container">
    <div class="flex-box">
      <img src="../assets/icon-badmintonBall.png">
      <h2>羽毛球</h2>
    </div>
    <div class="info-container-border"></div>
  </div>
  <div v-if="badmintonGames.length === 0 || !badmintonGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">

    <div class="games">
      <div class="game-card" v-for="game in badmintonGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="upcoming-game-name">{{ game.gameName }} ({{ game.gameStart }})</div>
          <div class="upcoming-game-info-container">
            <div class="campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container" :to="{ name:'playgroundList', params: {sportsEvent : 'BADMINTON'}}" >
    <button class="more-button">更多</button>
  </RouterLink>

  <div class="info-container">
    <div class="flex-box">
      <img src="../assets/icon-tennisBall.png">
      <h2>网球</h2>
    </div>
    <div class="info-container-border"></div>
  </div>
  <div v-if="tennisGames.length === 0 || !tennisGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in tennisGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="upcoming-game-name">{{ game.gameName }} ({{ game.gameStart }})</div>
          <div class="upcoming-game-info-container">
            <div class="campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container" :to="{ name:'playgroundList', params: {sportsEvent : 'TENNIS'}}" >
    <button class="more-button">更多</button>
  </RouterLink>

  <div class="info-container">
    <div class="flex-box">
      <img src="../assets/icon-tableTennisBall.png">
      <h2>乒乓球</h2>
    </div>
    <div class="info-container-border"></div>
  </div>
  <div v-if="tableTennisGames.length === 0 || !tableTennisGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in tableTennisGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="upcoming-game-name">{{ game.gameName }} ({{ game.gameStart }})</div>
          <div class="upcoming-game-info-container">
            <div class="campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}" >
    <button class="more-button">更多</button>
  </RouterLink>


</template>


<script setup>
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";


const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const upcomingGames = ref([{
  playgroundId: 1,
  gameId: 1,
}]);


onMounted(async () => {
  await getUpcomingGames()
});

const getUpcomingGames = async () => {
  await validateAccessToken()
  await axios.get(`${apiBaseUrl}/school/1/upcoming`,
      {
        headers: {
          'Authorization': getAccessToken()
        }
      }
  ).then(response => {
    upcomingGames.value = response.data.map(game => ({
      ...game,
    }));
  });
};

const sports = [
  {
    name: '足球',
    eventType: 'SOCCER',
    icon: '../assets/icon-soccerball.png',
    games: computed(() => upcomingGames.value.filter(game => game.sportsEvent === 'SOCCER'))
  },
  {

    name: '篮球',
    eventType: 'BASKETBALL',
    icon: '../assets/icon-basketballBall.png',
    games: computed(() => upcomingGames.value.filter(game => game.sportsEvent === 'BASKETBALL'))
  },
  {
    name: '羽毛球',
    eventType: 'BADMINTON',
    icon: '../assets/icon-badmintonBall.png',
    games: computed(() => upcomingGames.value.filter(game => game.sportsEvent === 'BADMINTON'))
  },
  {
    name: '乒乓球',
    eventType: 'TABLE_TENNIS',
    icon: '../assets/icon-tableTennisBall.png',
    games: computed(() => upcomingGames.value.filter(game => game.sportsEvent === 'TABLE_TENNIS'))
  },
  {
    name: '网球',
    eventType: 'TENNIS',
    icon: '../assets/icon-badmintonBall.png',
    games: computed(() => upcomingGames.value.filter(game => game.sportsEvent === 'TENNIS'))
  },
  // ... (다른 스포츠 추가)
];

const soccerGames = computed(() => {
  return upcomingGames.value.filter(game => game.sportsEvent === 'SOCCER');
});

const basketballGames = computed(() => {
  return upcomingGames.value.filter(game => game.sportsEvent === 'BASKETBALL');
});

const badmintonGames = computed(() => {
  return upcomingGames.value.filter(game => game.sportsEvent === 'BADMINTON');
});

const tennisGames = computed(() => {
  return upcomingGames.value.filter(game => game.sportsEvent === 'TENNIS');
});

const tableTennisGames = computed(() => {
  return upcomingGames.value.filter(game => game.sportsEvent === 'TABLE_TENNIS');
});

const validateAccessToken = async () => {
  const accessToken = getAccessToken();
  if (!accessToken) {
    await redirectToLogin()
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
  const refreshToken = getAccessToken()
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
    await redirectToLogin();
  }
};


const redirectToLogin = async () => {
  await router.push({name: 'login'});
};


</script>

<style scoped>
a {
  text-decoration: none;

}

.upcoming-games-container {
  margin: 0px 40px;
  min-width: 1100px;
  width: 90%;
  line-height: 1.4;
}

.flex-box {
  display: flex;
  align-content: center;
}

.flex-box img {
  width: 50px;
  height: 50px;
}

.flex-box h2 {
  margin: auto 10px;
  color: var(--accent-color);
}

.info-container {
  margin: 10px 30px;
}

.info-container h2 {
  font-size: 1.8rem;
  color: #333;

  text-align: start;
  font-family: MiSans-Heavy, sans-serif;
}

.info-container-border {
  margin-left: 200px;
  margin-right: 100px;
  width: 70%;
  max-width: 1200px;
  border-bottom: 1px solid var(--text-hint);
}

.info-container p {
  font-size: 0.8rem;
  color: #666;
  margin-right: auto;
  margin-left: 100px;
  margin-bottom: 20px;
  font-family: MiSans-Normal, sans-serif;
}

.campus-name {
  font-size: 14px;
  color: #4c8ba8;
}


.games {
  display: flex;
  width: 90%;
  min-width: 1100px;
  gap: 20px;
  justify-content: start;
}

.game-card {
  margin: 10px;
  max-width: 30%;
  background-color: var(--white);
  border: 1px solid #ddd;
  padding: 12px 15px;
  border-radius: 4px;
}

.upcoming-game-name {
  margin-bottom: 5px;
  overflow-wrap: break-word; /* 긴 단어가 컨테이너 너비를 초과할 경우 줄바꿈 */
  letter-spacing: 1px;
  color: var(--text-primary);
}

.upcoming-game-info-container {
  font-size: 11px;
  color: var(--text-primary);
  font-family: MiSans-Normal, sans-serif;
}

.game-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 약간의 그림자 효과 추가 */
  transition: transform 0.3s ease; /* 호버 효과를 위한 전환 설정 */
  transform: translateY(-5px); /* 호버 시 카드가 약간 위로 올라가는 효과 */
}

.more-button-container {
  display: flex;
  justify-content: center; /* 가로 방향으로 중앙 정렬 */
  align-items: center; /* 세로 방향으로 중앙 정렬 */
}

.more-button {
  width: 90%;
  padding: 10px 20px; /* 버튼 내부 여백 조정 */
  margin: 10px 0; /* 버튼 상하 여백 조정 */
  background-color: var(--secondary-color); /* 버튼 배경색, 변수에 따라 다를 수 있음 */
  color: #ffffff; /* 버튼 글자색 */
  border: none; /* 테두리 제거 */
  border-radius: 5px; /* 버튼 모서리 둥글게 */
  font-family: MiSans-Semibold, sans-serif; /* 폰트 지정 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

.more-button:hover {
  background-color: skyblue/* 마우스 오버 시 버튼 배경색 변경 */
}

.empty-games-screen {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px; /* 또는 적절한 높이 */
  background-color: #fff; /* 흰색 배경 */
  margin: 10px 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.empty-message {
  text-align: center;
}

.empty-message h2 {
  color: #666;
  font-family: MiSans-Medium, sans-serif;
}

.empty-message p {
  color: #999;
  font-family: MiSans-Normal, sans-serif;
}

</style>