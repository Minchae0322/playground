<template>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
  </head>

  <div class="campus-button-container-playgroundList">
    <button :class="{ active: activeCampus === null }" @click="getWholePlaygroundAndUpcomingGames(props.sportsEvent)">
      ALL
    </button>
    <div v-for="campus in campusInfo" :key="campus.campusId">
      <button :class="{ active: activeCampus === campus.campusId }"
              @click="getCampusPlaygroundAndUpcomingGames(campus.campusId, props.sportsEvent)"> {{ campus.campusName }}
      </button>
    </div>
  </div>

  <div class="upcoming-games-container">
    <div class="page-title-container">
      <h2>即将开始比赛</h2>
      <p>Check out our playground</p>
      <div class="page-title-container-border"></div>
    </div>
    <div class="games-home">
      <div class="game-card-mini" v-for="game in upcomingGames" :key="game.id">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
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
  <div v-if="isPlaygroundExist" class="playground-list">
    <div v-for="info in playgroundInfoList" :key="info.playgroundId" class="playground-card">
      <div class="card-header">
        <img :src="info.playgroundProfileImg" alt="game image" class="game-image"/>
        <div class="playground-name">{{ info.playgroundName }}</div>
        <div class="playground-info-container">
          <div class="sports-event-playgroundList">
            <img class="sports-event-img"
                 src="@/assets/icon-olympic.png">
            <div class="sports-event-text">{{ info.sportsEvent }}</div>
              <img class="location-img-playgroundList"
                   src="@/assets/icon-location-black.png">
              <div class="location-text">{{ info.campusName }}</div>
          </div>
        </div>
      </div>
      <div class="card-body">
        <div class="game-info-container">
          <div>预定的比赛 : {{ info.upcomingGameNum }}</div>
        </div>
      </div>
      <div class="card-footer">
        <router-link :to="{ name: 'playground' , params : {playgroundId: info.playgroundId, receivedGameId: 0}}">
          <button>view</button>
        </router-link>
      </div>
    </div>

  </div>
  <div v-else>
    <div class="teamRequest-notExist">运动场不存在</div>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref, watch} from 'vue';
import axios from "axios";
import GameBuilderModal from '../game/GameBuilderView.vue';
import {useRouter} from "vue-router";
import {defineEmits} from 'vue';
import defaultImage from '../../assets/img.png';

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
const isPlaygroundExist = ref(true);
const playgroundInfoList = ref([{
  playgroundId: 1,
}]);
const upcomingGames = ref([{
  playgroundId: 1,
  gameId: 1,
}]);
const campusInfo = ref([{}]);
const activeCampus = ref(null);

const props = defineProps({
  sportsEvent: {
    type: String,
    required: true,
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

watch(() => props.sportsEvent, (newSportsEvent, oldSportsEvent) => {
  if (newSportsEvent !== oldSportsEvent) {
    getWholePlaygroundAndUpcomingGames(newSportsEvent)
  }
}, {immediate: true});
</script>

<style scoped>
.campus-button-container-playgroundList {
  display: flex; /* 이 부분을 변경하여 flex 컨테이너로 만듭니다. */
  align-items: center; /* 버튼을 세로 중앙에 정렬합니다. 필요하지 않다면 제거 가능합니다. */
  justify-content: flex-start; /* 버튼을 가로 방향으로 왼쪽 정렬합니다. */
  width: 90%;
  margin-left: 20px;
  background-color: var(--white);
  border-radius: 5px;
  overflow: hidden;
  min-width: 1100px;
  padding: 5px 20px;
}

.campus-button-container-playgroundList button {
  background-color: var(--background-color-gray);
  border: none;
  width: 70px;
  height: 30px;
  margin: auto 0;
  padding: 5px 20px;
  color: var(--text-hint-dark);
  cursor: pointer;
  font-size: 10px;
  font-family: MiSans-Medium, sans-serif;
}

.campus-button-container-playgroundList button:hover {
  background-color: #f0f0f0;
}

.campus-button-container-playgroundList button.active {
  background-color: var(--white);
  color: #000;
}


.teamRequest-notExist {
  text-align: center;
  width: 100%;
  margin-top: 200px;
  align-content: center;
  justify-content: center;
}

.upcoming-games-container {
  min-width: 1100px;
  width: 95%;
  line-height: 1.4;
}

.games {
  display: flex;
  width: 90%;
  margin: 0 auto;
  min-width: 1100px;
  gap: 20px;
  justify-content: start;
}


.game-card-mini-info-container {
  font-size: 11px;
  color: var(--text-primary);
  font-family: MiSans-Normal, sans-serif;
}


.playground-list {
  display: flex;
  min-width: 1045px;
  width: 95%;
  flex-wrap: wrap; /* 내용이 넘치면 다음 줄로 넘깁니다. */
  margin: 10px auto;
  padding: 20px;

}

.playground-card {
  background-color: #fff; /* 카드 배경색 */
  border-radius: 8px; /* 모서리 둥글게 */
  margin-bottom: 20px; /* 카드 간 간격 */
  margin-right: 20px;
  width: 250px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}


.card-header {
  padding: 15px 20px 5px 20px;
  border-bottom: 1px solid #eee; /* 헤더 하단 선 */
}

.game-image {
  width: 100%;
  height: 100%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 12; /* 18:9 비율로 설정 */
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
  padding: 10px 15px; /* Add some padding inside the card */
  background-color: var(--background-color-gray); /* Set a background color */
  border-radius: 8px; /* Optional: rounded corners */
  cursor: pointer; /* Indicates it's clickable */
  font-size: 11px
}

.sports-event-playgroundList {
  display: flex;
  margin: auto 0;

}

.sports-event-img {
  width: 20px;
  margin-right: 5px;
}

.sports-event-text {
  margin: auto 0;
  color: black;
}


.location-img-playgroundList {
  width: 15px;
  margin: auto 0 auto 10px;
  height: 15px;
}

.location-text {
  margin: auto 0;
  color: black;
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
  background-color: var(--secondary-color); /* 버튼 호버 색상 */
}

@media (max-width: 600px) {
  .campus-button-container-playgroundList {
    min-width: 400px;
    width: 95%;
    margin: 0 auto;
  }

  .upcoming-games-container {
    min-width: 400px;
    width: 100%;
    display: flex;
    justify-content: center;
    flex-direction: column;
    margin: 0 auto;
  }

  .games {
    margin: 0 auto;
    min-width: 400px;
    width: 95%;
    padding-left: 20px;
    display: flex;

    flex-wrap: wrap;
  }


  .playground-list {
    min-width: 400px;
    width: 100%;
    padding: 0;
    gap: 5px;
    flex-wrap: wrap;
  }

  .playground-card {
    width: 45%;
    margin: 10px auto;
  }


}


</style>