<template>
  <div class="button-group">
    <button :class="{ active: activeCampus === null }" @click="getWholePlaygroundAndUpcomingGames(props.sportsEvent)">ALL</button>
    <div v-for="campus in campusInfo" :key="campus.campusId">
      <button :class="{ active: activeCampus === campus.campusId }" @click="getCampusPlaygroundAndUpcomingGames(campus.campusId, props.sportsEvent)"> {{ campus.campusName }} </button>
    </div>
  </div>

  <div class="upcoming-games-container">
    <h2>即将开始比赛</h2>
    <p>Check out our upcoming games.</p>
    <div class="upcoming-games-border"></div>
    <div class="games">
      <div class="game-card" v-for="game in upcomingGames" :key="game.id">
        <div class="upcoming-game-name">{{ game.gameName }}</div>
        <div class="upcoming-game-info-container">
        <div>Host: {{ game.hostName }}</div>
        <div>Start Time: {{ game.gameStart }} | Running Time: {{ game.runningTime }} </div>
        </div>
      </div>
    </div>
  </div>
  <div v-if="isPlaygroundExist" class="playground-list">
    <div v-for="info in playgroundInfoList" :key="info.playgroundId" class="playground-card">

      <div class="card-header">
        <img :src="info.playgroundProfileImg" alt="game image" class="game-image"/>
        <div class="playground-name">{{ info.playgroundName }}</div>
        <div class="playground-info-container">
          <div>{{ info.sportsEvent }}</div>
          <div>{{ info.campusName }}</div>
        </div>

      </div>
      <div class="card-body">
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
      </div>
      <div class="card-footer">
        <router-link :to="{ name: 'playground' , params : { playgroundId: info.playgroundId}}">
          <button>view</button>
        </router-link>
      </div>
    </div>

  </div>
  <div v-else>
    <div class="playground-notExist">활성화된 운동장이 존재하지 않습니다.</div>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import GameBuilderModal from '../game/GameBuilderView.vue';
import {useRouter} from "vue-router";
import { defineEmits } from 'vue';

const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const isPlaygroundExist = ref(true);
const playgroundInfoList = ref([{
  playgroundId: 1,
}]);
const upcomingGames = ref([{}]);
const campusInfo = ref([{}]);
const activeCampus = ref(null);

const props = defineProps({
  sportsEvent: {
    type: String,
    required:true,
  }
});


onMounted(async () => {
  await getCampusInfo()
})


const getCampusInfo = async () => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/school/1/campus/info`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    campusInfo.value = response.data.map(campus => ({
      ...campus,
    }));
  } catch (error) {
  }
};

const getPlaygroundsByCampus = async (campusId, sportsEvent) => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/playground/${campusId}/${sportsEvent}`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    playgroundInfoList.value = response.data.map(playground => ({
      ...playground,
      playgroundProfileImg: playground.playgroundProfileImg ? `data:image/jpeg;base64,${playground.playgroundProfileImg}` : defaultImage,
    }));
    isPlaygroundExist.value = true;

  } catch (error) {
    isPlaygroundExist.value = false;
  }
  activeCampus.value = campusId;
};

const getWholePlaygroundAndUpcomingGames = (sportsEvent) => {
  getPlaygrounds(sportsEvent)
  getUpcomingGames(sportsEvent)
}

const getCampusPlaygroundAndUpcomingGames = (campusId, sportsEvent) => {
  getPlaygroundsByCampus(campusId, sportsEvent)
  getUpcomingGamesByCampusId(campusId, sportsEvent)
}

const getPlaygrounds = async (sportsEvent) => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/school/1/playground/${sportsEvent}`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    playgroundInfoList.value = response.data.map(playground => ({
      ...playground,
      playgroundProfileImg: playground.playgroundProfileImg ? `data:image/jpeg;base64,${playground.playgroundProfileImg}` : defaultImage,
    }));
    isPlaygroundExist.value = true;

    } catch (error) {
    isPlaygroundExist.value = false;
  }
  activeCampus.value = null;
};

const getUpcomingGames = async (sportsEvent) => {
  await validateAccessToken()
  await axios.get(`${apiBaseUrl}/school/1/upcoming/${sportsEvent}`,
      {  headers: {
          'Authorization': getAccessToken()
        }}
  ).then(response => {
    upcomingGames.value = response.data.map(game => ({
      ...game,
    }));
  });
};

const getUpcomingGamesByCampusId = async (campusId, sportsEvent) => {
  await validateAccessToken()
  await axios.get(`${apiBaseUrl}/campus/${campusId}/upcoming/${sportsEvent}`,
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

watch(() => props.sportsEvent, (newSportsEvent, oldSportsEvent) => {
  if (newSportsEvent !== oldSportsEvent) {
   getWholePlaygroundAndUpcomingGames(newSportsEvent)
  }
}, { immediate: true });
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif; /* 기본 글꼴 */

  color: #333; /* 기본 텍스트 색상 */
}

.button-group {
  display: flex; /* 이 부분을 변경하여 flex 컨테이너로 만듭니다. */
  align-items: center; /* 버튼을 세로 중앙에 정렬합니다. 필요하지 않다면 제거 가능합니다. */
  justify-content: flex-start; /* 버튼을 가로 방향으로 왼쪽 정렬합니다. */
  margin-left: 20px;
  background-color: var(--white);
  border-radius: 5px;
  margin-right: 20px;
  overflow: hidden;
  padding: 10px 20px;
}

.button-group button {
  background-color: var(--background-color-gray);
  border: none;
  width: 70px;
  height: 30px;
  padding: 5px 20px;
  color: var(--text-hint-dark);
  cursor: pointer;
  font-size: 10px;
  font-family: MiSans-Medium, sans-serif;
}

.button-group button:hover {
  background-color: #f0f0f0;
}

.button-group button.active {
  background-color: var(--white);
  color: #000;
}


.playground-notExist {
  text-align: center;
  width: 100%;
  margin-top: 200px;
  align-content: center;
  justify-content: center;
}

.upcoming-games-container {
  margin: 10px 40px;
}

.upcoming-games-container h2 {
  font-size: 1.8rem;
  color: #333;

  text-align: start;
  font-family: MiSans-Heavy, sans-serif;
}

.upcoming-games-border {
  margin-left: auto;
  margin-right: 100px;
  width: 70%;
  border-bottom: 1px solid var(--text-hint);
}

.upcoming-games-container p {
  font-size: 0.8rem;
  color: #666;
  margin-right: auto;
  margin-left: 100px;
  margin-bottom: 20px;
  font-family: MiSans-Normal,sans-serif;
}

.games {
  display: flex;
  width: 90%;
  min-width: 1100px;
  gap: 20px;
  justify-content: start;
}

.game-card {
  margin-top: 10px;
  background-color: var(--white);
  border: 1px solid #ddd;
  padding: 12px 15px;
  border-radius: 4px;
  width: 33%; /* 각 카드의 너비를 30%로 설정하여 가로로 3개가 들어갈 수 있도록 함 */
  text-align: left; /* 텍스트를 왼쪽으로 정렬 */
}

.upcoming-game-name {
  margin-bottom: 5px;
  color: var(--text-primary);
}

.upcoming-game-info-container {
  font-size: 11px;
  color: var(--text-primary);
  font-family: MiSans-Normal, sans-serif;
}

.game-card:hover {
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* 약간의 그림자 효과 추가 */
  transition: transform 0.3s ease; /* 호버 효과를 위한 전환 설정 */
}

.game-card:hover {
  transform: translateY(-5px); /* 호버 시 카드가 약간 위로 올라가는 효과 */
}

.playground-list {
  width: 1200px;
  margin: 20px auto; /* 중앙 정렬 */
  padding: 20px;
}

.playground-card {
  background-color: #fff; /* 카드 배경색 */
  border-radius: 8px; /* 모서리 둥글게 */
  margin-bottom: 20px; /* 카드 간 간격 */
  width: 250px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1); /* 그림자 효과 */
}


.card-header {
  padding: 20px;
  border-bottom: 1px solid #eee; /* 헤더 하단 선 */
}

.game-image {
  width: 100%;
  height:100%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 10; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  opacity: 0.7; /* 이미지 투명도 */

}

.playground-name {
  font-size: 18px;
  text-align: center;
  color: var(--text-primary);
}

.playground-info-container {
  font-size: 13px;
  color: var(--text-hint);

}

.game-info-container {
  padding: 15px; /* Add some padding inside the card */

  background-color: var(--background-color-gray); /* Set a background color */
  border-radius: 8px; /* Optional: rounded corners */
  cursor: pointer; /* Indicates it's clickable */
  font-size: 0.6em;
}


.card-body {
  padding: 10px;
}

.card-footer {
  padding: 10px;
  background-color: #f9f9f9; /* 푸터 배경색 */
}

.card-footer button {
  background-color: var(--text-primary);
  width: 100%;
}

h2 {
  color: #000; /* 제목 색상 */
  margin-bottom: 10px;
}

h3 {
  color: #333; /* 부제목 색상 */
}

ul {
  list-style-type: none; /* 기본 리스트 스타일 제거 */
}

li {
  padding: 10px 0; /* 리스트 아이템 패딩 */
  border-bottom: 1px solid #eee; /* 아이템 하단 선 */
}

button {
  color: white; /* 버튼 텍스트 색상 */
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 4px; /* 버튼 모서리 둥글게 */

}

button:hover {
  background-color: #4cae4c; /* 버튼 호버 색상 */
}

/* 반응형 디자인: 화면 크기에 따라 스타일 조정 */
@media (max-width: 768px) {

}

</style>