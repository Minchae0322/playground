<template>
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
        <div v-if="currentGame">
          <div><strong>Host</strong> {{ currentGame.hostName }}</div>
          <div><strong>StartTime</strong> {{ currentGame.gameStart }}</div>
          <div><strong>Running Time</strong> {{ currentGame.time }}</div>
        </div>
        <div v-else>
          <div>No current game in progress</div>
        </div>
      </div>

      <button class="join-button" @click="openGameBuilder">Join Game</button>
    </div>
    <div class="upcoming-container" id="gameView-container">
      <component :is="currentView" :game="selectedGame" key="selectedGame.gameId"
                 @gameSelected="handleGameSelected"
                 @goBack="handleGoBack"></component>
    </div>
    <GameBuilderModal v-if="isGameBuilderModalOpen" :some-data="data" @closeGameBuilder=closeModal></GameBuilderModal>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import GameBuilderModal from '../game/GameBuilderView.vue';
import {useRouter} from "vue-router";
import defaultImage from '../../assets/img.png';
import PlaygroundInfoView from "@/views/playground/PlaygroundInfoView.vue";
import GameInfoView from '../game/GameInfoView.vue';

const data = ref('이것은 부모로부터 온 데이터입니다.');
const playgroundInfo = ref({
  playgroundProfileImg: '',

})

const props = defineProps({
  playgroundId: {
    type: Number,
    required: true,
  }
})
const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const currentView = ref(PlaygroundInfoView); // 초기 뷰 설정
const selectedGame = ref(null); // 선택된 게임
const isGameBuilderModalOpen = ref(false);

onMounted(async () => {
  await getPlaygroundInfo()
});

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

const openGameBuilder = function () {
  isGameBuilderModalOpen.value = !isGameBuilderModalOpen.value;
};

const closeModal = () => {
  isGameBuilderModalOpen.value = false;
};

function handleGameSelected(game) {
  selectedGame.value = game; // 선택된 게임 업데이트
  currentView.value = GameInfoView; // 뷰를 GameInfoView로 변경
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
  min-width: 500px;
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

.game-info-container {
  height: 200px;
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

.playground-info-container {
  margin-left: 10px;
  text-align: start;
  font-size: 10px;
  font-family: MiSans-Normal, sans-serif;
}

.playground-name {
  font-size: 15px;
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
  height: 90vh;
  overflow-y: auto;
}


a {
  text-decoration: none;
}
</style>