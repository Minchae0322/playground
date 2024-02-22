<template>
  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-soccerball.png">
      <h2>足球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>

  <div v-if="soccerGames.length === 0 || !soccerGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>


  <div v-else class="upcoming-games-container-home">
    <div class="games-home">
      <div class="game-card-mini" v-for="game in soccerGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
          <div :style="{ color: getDayColor(game.gameStart) }">{{getChineseDayOfWeek(game.gameStart)}}</div></div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div class="game-card-mini-info">主持人: {{ game.hostName }}</div>
            <div class="game-card-mini-info">所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'SOCCER'}}" >
    <button class="more-button-home">更多</button>
  </RouterLink>


  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-basketballBall.png">
      <h2>篮球</h2>
    </div>
    <div class="page-title-container-border"></div>
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
          <div class="game-card-mini-title">{{ game.gameName }} {{getChineseDayOfWeek(game.gameStart)}}</div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}" >
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-badmintonBall.png">
      <h2>羽毛球</h2>
    </div>
    <div class="page-title-container-border"></div>
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
          <div class="game-card-mini-title">{{ game.gameName }} {{getChineseDayOfWeek(game.gameStart)}}</div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>


  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BADMINTON'}}" >
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-tennisBall.png">
      <h2>网球</h2>
    </div>
    <div class="page-title-container-border"></div>
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
          <div class="game-card-mini-title">{{ game.gameName }} {{getChineseDayOfWeek(game.gameStart)}}</div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'TENNIS'}}" >
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-tableTennisBall.png">
      <h2>乒乓球</h2>
    </div>
    <div class="page-title-container-border"></div>
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
          <div class="game-card-mini-title">{{ game.gameName }} {{getChineseDayOfWeek(game.gameStart)}}</div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}" >
    <button class="more-button-home">更多</button>
  </RouterLink>


</template>


<script setup>
import {computed, getCurrentInstance, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
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

const getChineseDayOfWeek = (dateString) => {
  const daysOfWeek = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
  const date = new Date(dateString);
  const dayOfWeekIndex = date.getDay();
  return daysOfWeek[dayOfWeekIndex];
};

const getDayColor = (dateString) => {
  const dayOfWeekIndex = new Date(dateString).getDay();
  // 각 요일에 대한 색상 매핑
  const colorMap = {
    0: 'red',    // 일요일
    1: 'blue',   // 월요일
    2: 'green',  // 화요일
    3: 'purple', // 수요일
    4: 'orange', // 목요일
    5: 'brown',  // 금요일
    6: 'teal',   // 토요일
  };
  return colorMap[dayOfWeekIndex] || 'black';
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
    await redirectToLogin();
  }
};


const redirectToLogin = async () => {
  await router.push({name: 'login'});
};


</script>

<style>
.upcoming-games-container-home {
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

.page-title-container {
  margin: 20px 30px;
}

.page-title-container img {
  width: 40px;
  height: 40px;
  margin-left: 10px;
}

.page-title-container h2 {
  font-size: 1.8rem;
  color: #333;
  text-align: start;
  font-family: MiSans-Heavy, sans-serif;
}

.page-title-container-border {
  margin-left: 200px;
  margin-right: 100px;
  width: 70%;
  min-width: 400px;
  margin-bottom: 30px;
  max-width: 800px;
  border-bottom: 1px solid var(--text-hint);
}

.page-title-container p {
  font-size: 0.8rem;
  color: #666;
  margin-right: auto;
  margin-left: 100px;
  margin-bottom: 10px;
  font-family: MiSans-Normal, sans-serif;
}



.game-card-mini-campus-name {
  font-size: 15px;
  margin: 5px 0;
  color: #4c8ba8;
}


.games-home {
  display: flex;
  width: 90%;
  flex-wrap: wrap;
  min-width: 1100px;
  gap: 20px;
  justify-content: start;
}

.game-card-mini {
  width: 30%;
  background-color: var(--white);
  border: 1px solid #ddd;
  padding: 12px 15px;
  flex-wrap: wrap;
  border-radius: 4px;
}

.game-card-mini-title {
  font-size: 17px;
  display: flex;
  gap: 10px;
  font-family: MiSans-Semibold, sans-serif;
  overflow-wrap: break-word; /* 긴 단어가 컨테이너 너비를 초과할 경우 줄바꿈 */
  letter-spacing: 1px;
  color: var(--text-primary);
}

.game-card-mini-info {
  font-size: 12px;
  font-family: MiSans-Medium,sans-serif;
}

.more-button-container-home {
  display: flex;
  justify-content: center; /* 가로 방향으로 중앙 정렬 */
  align-items: center; /* 세로 방향으로 중앙 정렬 */
}

.more-button-home {
  width: 90%;
  padding: 10px 20px; /* 버튼 내부 여백 조정 */
  margin: 20px 0; /* 버튼 상하 여백 조정 */
  background-color: var(--secondary-color); /* 버튼 배경색, 변수에 따라 다를 수 있음 */
  color: #ffffff; /* 버튼 글자색 */
  border: none; /* 테두리 제거 */
  border-radius: 5px; /* 버튼 모서리 둥글게 */
  font-family: MiSans-Semibold, sans-serif; /* 폰트 지정 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

.more-button-home:hover {
  background-color: skyblue/* 마우스 오버 시 버튼 배경색 변경 */
}

.empty-games-screen {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px; /* 또는 적절한 높이 */
  background-color: #fff; /* 흰색 배경 */
  margin: 10px auto;
  width: 90%;
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

@media (max-width: 600px) {
  .page-title-container {
    min-width: 400px;
    margin: 0 auto;
    padding-left: 10px;
    width: 95%;
  }

  .games-home {
    min-width: 400px;
    width: 100%;
    padding-left: 20px;
    justify-content: start;
  }

  .game-card-mini {
    width: 45%;

  }

  .upcoming-games-container-home {
    min-width: 300px;
    width: 100%;
    margin: 0;
  }

  .page-title-container-border {
    min-width: 70%;
    margin-left: 20%;
    margin-bottom: 10px;
    margin-right: 0;
    width: 75%;
  }

  .game-card-mini-title {
    font-size: 14px;
  }
  .game-card-mini-campus-name {
    font-size: 12px;
    margin: 1px 0;
  }
  .game-card-mini-info {
    font-size: 12px;
  }
  .more-button-home {
    margin: 15px 0;
  }
}
</style>