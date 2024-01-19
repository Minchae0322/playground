<template>
  <div class="game-container">
    <div class="game-image">
      <!-- Your image goes here -->
      <img :src="playgroundInfo.playgroundProfileImg" alt="Game Image">
      <div class="overlay">
        <h1>{{ playgroundInfo.playgroundName }}</h1>
        <div class="playground-info-container">
        <p>{{ playgroundInfo.schoolName }} | {{playgroundInfo.campusName}}</p>
        <p>{{ playgroundInfo.sportsEvent }}</p>
        </div>
      </div>
    </div>
    <div id="gameView-container">
      <component :is="currentView" :game="selectedGame" key="selectedGame.gameId"
                 @gameSelected="handleGameSelected"
                 @goBack="handleGoBack"></component>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import GameBuilderModal from '../GameBuilderView.vue';
import {useRouter} from "vue-router";
import defaultImage from '../../assets/img.png';
import PlaygroundInfoView from "@/views/playground/PlaygroundInfoView.vue";
import GameInfoView from '../GameInfoView.vue';

const data = ref('이것은 부모로부터 온 데이터입니다.');
const playgroundInfo = ref({
  playgroundProfileImg: '',

})

const props = defineProps({
  playgroundId: {
    type: Number,
    required:true,
  }
})
const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const currentView = ref(PlaygroundInfoView); // 초기 뷰 설정
const selectedGame = ref(null); // 선택된 게임



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


.game-container {
  max-width: 70vw; /* Adjust the maximum width to your preference */
  width: 77vw; /* This will make the container take up 100% of its parent up to max-width */
  margin: auto auto 0;
  padding: 0; /* Padding 제거 */
  border-radius: 10px;

}

.game-image {
  position: relative;
  width: 100%;
  height: auto; /* 이미지의 높이를 자동으로 조정 */

}
/* ... other styles ... */

.game-image img {
  width: 100%;
  height:100%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 7; /* 18:9 비율로 설정 */
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
  width: 30%;
  padding: 10px 10px 10px 10px;
  text-align: center;
}

.playground-info-container {
  margin-left: 10px;
  text-align: start;
  font-family: MiSans-Normal,sans-serif;
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


}

.game-card p {

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