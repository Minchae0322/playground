<template>
  <div class="friendly-game-container">
    <div class="game-info-container">
      <div class="game-details">
        <div class="game-details-name">{{ props.game.gameName }} ( {{ props.game.gameType }} )</div>
        <div class="game-details-startTime">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
               stroke-width="3" stroke-linecap="round" stroke-linejoin="round"
               class="time-icon">
            <circle cx="12" cy="12" r="10"></circle>
            <polyline points="12 6 12 12 16 14"></polyline>
          </svg>
          {{ props.game.gameStart }} 进行时间 {{ props.game.runningTime }}分
        </div>
        <div class="host-info-container">
          <img class="host-info" :src="getImageUrl(game.hostProfileImg) || defaultImage">
          <div class="host-name">{{ game.hostName }}</div>
        </div>
      </div>
    </div>


    <div class="team">
      <div class="button-container">
        <button class="button-goBack" @click="goBack"></button>
      </div>
      <div class="title-soloTeam">参加</div>
      <div class="team-details">
        <div class="participants-num">参与人数 {{ participants.length }}</div>
        <div class="line"></div>
        <div class="participant-container">
          <div class="team-member" v-for="(participant, userId) in participants" :key="participant.userId">
            <!--            <div v-if="participant.userId === loggedInUserId" class="close-marker"
                             @click="clickOutOfGame()">X
                        </div>-->
            <div v-if="participant.userId === loggedInUserId" class="close-marker" @click="clickOutOfGame">X</div>
            <img class="team-member-photo" :src="getImageUrl(participant.userProfileImg)">
            <p class="nickname-container-nickname-userInfo">{{ participant.userNickname }}</p>
            <p class="user-role">个人</p>
          </div>
        </div>
      </div>

    </div>
    <div class="relative add-button-container">
      <button
          class="add-button-friendly"
          @click="sendFriendlyGameJoinRequest"
      >
        {{ buttonText }}
      </button>

    </div>
  </div>
</template>

<script setup>
import {getCurrentInstance, inject, onMounted, onUpdated, ref} from "vue";
import axios from "axios";
import {defineEmits} from 'vue';
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";
import {defineProps} from 'vue';

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
const loggedInUserId = ref('');

const props = defineProps({
  game: {
    type: Object,
    required: true
  },
});

const buttonText = ref("参加")
const participants = ref([{
  userProfileImg: '',
}])
const emits = defineEmits(['goBack']);
const frontBaseUrl = inject('frontBaseUrl');
const getImageUrl = (file) => {
  return frontBaseUrl + file;
};


onMounted(async () => {
  await getTeamData("NONE",
      'Friendly')
  await getLoggedUserId()
});

const getTeamData = async (matchTeamSide, gameType) => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/game/${props.game.gameId}/${gameType}/${matchTeamSide}`, {
      headers: {
        'Authorization': getAccessToken()
      }
    });

    participants.value = response.data.participants.map(user => ({
      ...user,
      userProfileImg: user.userProfileImg ? user.userProfileImg : defaultImage,
    }));
    console.log(participants.value)
  } catch (error) {
    console.error(`Failed to fetch ${matchTeamSide} teams data:`, error);
    // 에러 처리 로직을 추가할 수 있습니다.
  }
};


function goBack() {
  emits('goBack'); // 부모 컴포넌트에게 뒤로가기 이벤트 전달
}


const getLoggedUserId = async () => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/user/info`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        });
    loggedInUserId.value = response.data.userId;
    // 서버에서 성공 응답을 받았을 때의 처리를 이곳에 추가할 수 있습니다.
  } catch (error) {
    showErrorMessage(error)
  }
};

const clickOutOfGame = async () => {
  const isConfirm = confirm("确定要退出吗？")
  if (!isConfirm) {
    return
  }
  await validateAccessToken();
  try {
    await axios.delete(`${apiBaseUrl}/game/${props.game.gameId}/friendly/out`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
    alert("退出成功")
    await getTeamData("NONE",
        'Friendly')
  } catch (error) {
    alert(error.response.data.message)
  }
};

const sendFriendlyGameJoinRequest = async () => {
  const isConfirm = confirm("确定进行参加？")
  if (!isConfirm) {
    return
  }
  await validateAccessToken();
  try {
    const response = await axios.post(`${apiBaseUrl}/game/join/friendlyGameJoin`, {
          gameTeamSide: 'NONE',
          athleticsId: props.game.gameId,
          gameType: 'friendlyAthleticsJoin'
        },
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
    alert("请求成功")
  } catch (error) {
    showErrorMessage(error)
  }
};

const showErrorMessage = (error) => {
  if (error.response && error.response.data && error.response.data.message) {
    alert(error.response.data.message);
  } else {
    // 에러 응답이 없거나 예상치 못한 에러의 경우
    alert("경기 참가 과정에서 에러가 발생했습니다.");
  }
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
</script>

<style scoped>

.friendly-game-container {
  text-align: center;
  align-items: center;
  justify-content: center;
  display: flow;
  flex-direction: column;
  background: var(--white);
}


.game-info-container {
  width: 100%;
  background: var(--white);
}


.game-details {
  font-family: MiSans-Medium, sans-serif;
  color: white;
  border-radius: 8px;
  text-align: start;
  font-size: 12px;

  padding: 10px 15px;
  background-color: var(--accent-color);
  letter-spacing: 1px;
}


.game-details-name {
  font-family: MiSans-Medium, sans-serif;
  font-size: 22px;

}

.game-details-startTime {
  display: flex;
  align-content: center;
}

.time-icon {
  align-content: center;
  justify-content: center;
  margin: auto 5px auto 0;
  height: 14px;
}

.host-info-container {
  display: flex;
  align-content: center;
  text-align: center;
  margin: 10px 5px 0 5px;
}

.host-info {
  border-radius: 50%;
  background-color: #ddd; /* 아이콘 배경색 설정 */
  display: inline-block;
  width: 40px; /* 아이콘 크기 조정 */
  height: 40px;
  margin-right: 8px; /* 아이콘과 텍스트 사이의 간격 조정 */
  vertical-align: middle; /* 수직 정렬 */
}

.host-name {
  margin: auto 0;
  font-family: MiSans-Medium, sans-serif;
  font-size: 14px;
}

.button-container {
  background-color: white;
  width: 100%;
  padding: 15px;
  margin-bottom: 10px;
  display: flex; /* flexbox 레이아웃 사용 */
  justify-content: space-between; /* 양쪽 끝에 요소를 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
}

.button-goBack {
  background: none; /* 배경 없음 */
  border: none; /* 테두리 없음 */
  color: black; /* 아이콘 색상 */
  font-size: 24px; /* 아이콘 크기 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  margin-right: auto;
}

.button-goBack::before {
  content: ''; /* 내용 없음 */
  border-top: 2px solid black; /* 위쪽 테두리 */
  border-left: 2px solid black; /* 왼쪽 테두리 */
  width: 15px; /* 가로 길이 */
  height: 15px; /* 세로 길이 */
  transform: rotate(-45deg); /* 45도 회전 */
  display: block; /* 블록 요소로 설정 */
}

.button-goBack span {
  visibility: hidden; /* 버튼 내 기존 텍스트 숨김 */
}

.participant-container {
  display: flex;
  flex-direction: row;

}

.team {
  background: var(--background-color-gray);
  justify-content: center;
}


.team-details {
  background: #ffffff; /* Light grey background */
  padding: 7px;
  margin: 10px auto 10px auto;
  display: flex;
  width: 100%;
  flex-direction: column;
  transition: box-shadow 0.3s ease;
  align-items: flex-start; /* Align items to the start of the flex container */
}


.line {
  width: 80%;
  padding: 0 0 5px 0;
  border-bottom: 1px solid #c7c7c7;
}

.participants-num {
  font-size: 11px;
  font-family: MiSans-Medium, sans-serif;
  padding: 10px 10px 0 10px;
  color: #838383;
  letter-spacing: 1px;
}


.team-member {
  display: flex;
  flex-direction: column;
  background: white;
  padding: 10px 5px 5px 5px;
  width: 10%;
  min-width: 70px;
  height: 120px;
  text-align: center;
  line-height: 1.1;
  border-radius: 6px;
}

.team-member-photo {
  width: 50px; /* 이미지 크기 조절 */
  height: 50px; /* 이미지 크기 조절 */
  object-fit: cover; /* 이미지 비율을 유지하면서 요소에 맞게 조정 */
  border-radius: 50%;
  margin: 4px auto 3px;
}

.team-member .nickname-container-nickname-userInfo {
  font-size: 13px;
  font-weight: 600;
  color: black;
  display: flex;
  align-items: center;
  justify-content: center;

}

.team-member .user-role {
  font-size: 11px;
  color: #838383;
}

.title-soloTeam {
  text-align: start;
  margin-left: 5px;
  color: black;
  font-size: 20px;
}

.relative {

  background-color: var(--background-color-gray);
}

.add-button-container {
  background-color: white;
  width: 100%;
  padding: 0 0 15px 0;
}

.add-button-friendly {
  width: 95%;
  border-radius: 4px;
  margin: 0px auto;
  font-size: 14px;
  cursor: pointer;
  outline: none;
  background: var(--accent-color);
  z-index: 1000; /* 메뉴가 버튼 아래에 오도록 z-index 설정 */
}

.add-button-friendly:hover {
  background-color: var(--secondary-color)
}

.close-marker {
  position: relative; /* 절대 위치 설정 */
  cursor: pointer; /* 마우스 포인터 스타일 변경 */
  /* 필요한 추가 스타일 */
}

</style>