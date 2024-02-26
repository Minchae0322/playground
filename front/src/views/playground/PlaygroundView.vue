<template>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>dd</title>
  </head>
  <div class="game-container">
    <div class="playground-container">
      <div class="game-image">
        <img :src="playgroundInfo.playgroundProfileImg" alt="Game Image">
        <div class="overlay">
          <div class="playground-name">{{ playgroundInfo.playgroundName }}</div>
          <div class="playground-info-container">
            <p>{{ playgroundInfo.schoolName }} | {{ playgroundInfo.campusName }}</p>
            <p>{{ playgroundInfo.sportsEvent }}</p>
          </div>
        </div>
      </div>
      <div class="game-info-container">
        <div class="ongoing-title">Ongoing game</div>
        <div v-if="ongoingGame.gameId">
          <div class="game-card">
            <div class="game-type-container">
              <img v-if="ongoingGame.gameType === '竞争'" :src="rankingIcon">
              <img v-else :src="friendlyIcon">
              <p class="game-type">{{ ongoingGame.gameType }}</p>
            </div>
            <div class="game-info">
              <div class="game-name">{{ ongoingGame.gameName }}</div>
              <div class="game-time">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="3" stroke-linecap="round" stroke-linejoin="round"
                     class="time-icon">
                  <circle cx="12" cy="12" r="10"></circle>
                  <polyline points="12 6 12 12 16 14"></polyline>
                </svg>
                {{ ongoingGame.gameStart }}（ {{ ongoingGame.runningTime }}分 ）
              </div>
              <div class="host-info">
                <img :src="ongoingGame.hostProfileImg || defaultImage">
                <div class="host-name">{{ ongoingGame.hostName }}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="ongoing-not-exist" >
          没有正在进行的比赛
        </div>
      </div>

      <button class="join-button" @click="openGameBuilder">新比赛</button>
    </div>
    <div class="upcoming-container" id="gameView-container">
      <component :is="currentView" :game="selectedGame" :key="componentKey" :playground-id="props.playgroundId"
                 @gameSelected="handleGameSelected"
                 @goBack="handleGoBack"></component>
    </div>
    <GameBuilderModal v-if="isGameBuilderModalOpen" :playground-id="props.playgroundId"  @closeGameBuilder=closeModal></GameBuilderModal>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, onUpdated, ref} from 'vue';
import axios from "axios";
import GameBuilderModal from '../game/GameBuilderView.vue';
import {useRouter} from "vue-router";
import defaultImage from '../../assets/img.png';
import PlaygroundInfoView from "@/views/playground/PlaygroundInfoView.vue";
import GameInfoView from '../game/GameInfoView.vue';
import FriendlyGameInfoView from "@/views/game/FriendlyGameInfoView.vue";
import friendlyIcon from '../../assets/icon-handshake.png'
import rankingIcon from '../../assets/icon-ranking.png'

const playgroundInfo = ref({
  playgroundProfileImg: '',

})
const componentKey = ref(0);
const props = defineProps({
  playgroundId: {
    type: Number,
    required: true,
  },
  receivedGameId: {
    type: Number,
    required: true,
  },

})

const ongoingGame = ref({
  hostProfileImg: ''
})
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
const currentView = ref(PlaygroundInfoView); // 초기 뷰 설정
const selectedGame = ref(null); // 선택된 게임
const isGameBuilderModalOpen = ref(false);

onMounted(async () => {
  await getPlaygroundInfo()
  await getOngoingGame();
  await clickInfo();
});


const clickInfo = async () => {
  console.log(props.receivedGameId)
  if (props.receivedGameId !== '0') {
    let game = ''
    await validateAccessToken()
    try {
      const response = await axios.get(`${apiBaseUrl}/game/${props.receivedGameId}/info`,
          {
            headers: {
              'Authorization': getAccessToken()
            }
          }
      )
      game = response.data;
      game.hostProfileImg = `data:image/jpeg;base64,${response.data.hostProfileImg}`;
    } catch (error) {

    }
    await handleGameSelected(game)
  }
}

const getPlaygroundInfo = async () => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/playground/${props.playgroundId}/info`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
    playgroundInfo.value = response.data
    playgroundInfo.value.playgroundProfileImg = `data:image/jpeg;base64,${response.data.playgroundProfileImg}`;


  } catch (error) {
    alert("운동장 정보를 불러오는데 실패했습니다.")
    router.go(-1);
  }
};

const getOngoingGame = async function () {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/playground/${props.playgroundId}/current`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
    ongoingGame.value = response.data
    ongoingGame.value.hostProfileImg = `data:image/jpeg;base64,${ongoingGame.value.hostProfileImg}`;
  } catch (error) {
  }
}

const calculateTimeRange = function() {
  if (!ongoingGame.value.gameStartDateTime || !ongoingGame.value.runningTime) {
    return "시간 정보 없음";
  }

  // 시작 시간 파싱
  const startTime = parseLocalDateTime(ongoingGame.value.gameStartDateTime);
  const formattedStartTime = formatDate(startTime);

  // 종료 시간 계산
  const runningMinutes = parseInt(ongoingGame.value.runningTime);
  const endTime = new Date(startTime.getTime() + runningMinutes * 60000);
  const formattedEndTime = formatDate(endTime);

  // 시작시간 ~ 종료시간 형태로 문자열 결합
  return `${formattedStartTime} ~ ${formattedEndTime}`;
};

const parseLocalDateTime = function(localDateTime) {
  const [datePart, timePart] = localDateTime.split('T');
  const [year, month, day] = datePart.split('-');
  const [hour, minute] = timePart.split(':');

  return new Date(year, month - 1, day, hour, minute);
};

const formatDate = function(date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hour = date.getHours().toString().padStart(2, '0');
  const minute = date.getMinutes().toString().padStart(2, '0');

  return `${year}-${month}-${day} ${hour}:${minute}`;
};
const openGameBuilder = function () {
  isGameBuilderModalOpen.value = !isGameBuilderModalOpen.value;
};

const closeModal = () => {
  isGameBuilderModalOpen.value = false;
  componentKey.value++; // key 값을 변경하여 컴포넌트를 재생성

};

function handleGameSelected(game) {
  selectedGame.value = game; // 선택된 게임 업데이트
  if (game.gameType === '竞争') {
    currentView.value = GameInfoView; // 뷰를 GameInfoView로 변경
  } else {
    currentView.value = FriendlyGameInfoView; // 뷰를 GameInfoView로 변경
  }



}

function handleGoBack() {
  currentView.value = PlaygroundInfoView; // 뷰를 PlaygroundInfoView로 변경
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
.game-container {
  display: flex;
  flex-direction: row;
  width: 96%; /* Adjust the maximum width to your preference */
  min-width: 1100px; /* This will make the container take up 100% of its parent up to max-width */
  margin: auto auto auto auto;
  padding: 20px; /* Padding 제거 */
  border-radius: 10px;
  height: 100%;
}

.playground-container {
  display: flex;
  flex-direction: column;
  min-width: 520px;
  width: 50%;
  height: 100%;
}

.game-image {
  position: relative;
  width: 100%;
  height: 45%;
}

/* ... other styles ... */

.game-image img {
  width: 100%;
  height: 100%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 12; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  display: block; /* 이미지를 블록 요소로 만들어 불필요한 여백 제거 */
}

.overlay {
  position: absolute;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
  color: white;
  width: 40%;
  padding: 10px 10px 10px 10px;
  text-align: center;
}

.ongoing-title {
  font-family: MiSans-Semibold, sans-serif;
  padding: 8px 0 0 10px;
}

.game-info-container {
  height: 220px;
  border: 1px solid #ddd; /* Add a border */
  margin-top: 10px; /* Add some space above the card */
  background-color: #fff; /* Set a background color */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Add a subtle shadow */
  border-radius: 8px; /* Optional: rounded corners */
  cursor: pointer; /* Indicates it's clickable */
  transition: box-shadow 0.3s; /* Smooth transition for hover effect */
}

.game-info-container:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* Slightly larger shadow on hover */
}

.playground-info-container {
  margin-left: 10px;
  text-align: start;
  font-size: 10px;
  font-family: MiSans-Normal, sans-serif;
}

.playground-name {
  font-size: 15px;
}
.ongoing-not-exist {
  margin: 50px auto 0 auto;
  font-family: MiSans-Normal,sans-serif;
  color: var(--text-hint-dark);
  font-size: 12px;
  justify-content: center;
  text-align: center;
}

.game-info {
  display: flex;
  flex-direction: column;
  align-content: end;
  justify-content: end;
  flex-basis: 67%;
  margin-left: auto;
}

.game-card {
  width: 90%;
  display: flex;
  border: 1px solid #ddd;
  padding: 15px 20px;
  font-size: 0.875rem;
  color: #666;
  margin: 10px auto;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05); /* 그림자를 더 부드럽게 조정 */
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.game-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 강조 */
}


.game-name {
  font-size: 19px;
  font-family: MiSans-Semibold, sans-serif;
}

.time-icon {
  margin: auto 7px auto 0;
  height: 15px;
}

.game-time {
  display: flex;
  font-size: 13px;
  align-content: center;
  color: var(--text-hint-dark);
  margin-top: 7px;
  font-family: MiSans-Medium, sans-serif;
}

.host-info {
  margin-top: 10px;
  display: flex; /* Flexbox를 이용한 가로 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  margin-bottom: 0.5rem; /* 아래쪽 여백 */
}

.host-name {
  font-family: MiSans-Medium, sans-serif;
}

/* 아이콘 스타일링 */
.host-info img {
  border-radius: 50%;
  background-color: #ddd; /* 아이콘 배경색 설정 */
  display: inline-block;
  width: 35px; /* 아이콘 크기 조정 */
  height: 35px;

  margin-right: 8px; /* 아이콘과 텍스트 사이의 간격 조정 */
  vertical-align: middle; /* 수직 정렬 */
}

/* 시간과 실행 시간 정보 스타일링 */

.game-type {
  font-size: 0.875rem;
  color: #666;

  text-align: center;
  align-content: center;
  justify-content: center;

}

/* 링크 스타일링 제거 */
.game-card a {
  text-decoration: none;
  color: inherit;
}

a {

  text-decoration: none;
}

.game-type-container {
  display: flex;
  flex-basis: 20%;
  flex-direction: column;
}

.game-type-container img {
  background: none;
  width: 80px; /* 아이콘 크기 */
  height: 80px;
  border-radius: 4px;
  margin: 0 auto;
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

.upcoming-container {
  margin-left: 20px;
  min-width: 500px;
  width: 50%;

  min-height: 350px;
  overflow-y: auto;
}



@media (max-width: 600px) {
 .game-container {
   display: flex;
   flex-direction: column;
   min-width: 300px;
   margin: 0;
   width: 100%;
 }

  .playground-container {
    min-width: 300px;
    width: 100%;
    margin: 0 0 20px;
  }

  .upcoming-container {
    min-width: 300px;
    width: 100%;
    margin-left: 0;
    margin-bottom: 50px;
  }



}
</style>