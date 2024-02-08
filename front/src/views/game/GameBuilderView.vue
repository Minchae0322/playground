<template>
  <div class="modal-overlay">
    <div class="event-details-container">
      <div class="title">创新比赛</div>
      <div class="title-description">请输入比赛事详情</div>

      <div class="form-container">
        <div class="form-group">
          <label for="eventName">比赛名</label>
          <input type="text" v-model="gameRegistration.gameName" id="eventName" name="eventName"
                 placeholder="请输入比赛名">
        </div>

        <div class="form-group">
          <label for="venue">项目</label>
          <div>{{ playgroundInfo.sportsEvent }}</div>
        </div>

        <div class="form-group">
          <label>开赛时间</label>
          <button type="button" id="eventDate" class="date-button" @click="clickSelectDate">
            {{ isStartTimeSelected ? formattedStartTime : '选择时间' }}
          </button>

          <ModalComponent v-if="isModalOpen" @close="isModalOpen = false" @updateValue="handleUpdateStartTime">
          </ModalComponent>
        </div>

        <div class="form-group radio-container">
          <div class="checkbox">
            <input type="radio" id="friendlyMatch" name="matchType" v-model="gameRegistration.gameType" value="Friendly"
                   checked>
            <label for="friendlyMatch">友谊赛</label>
          </div>
          <div class="checkbox">
            <input type="radio" id="competitionMatch" name="matchType" v-model="gameRegistration.gameType"
                   value="Competition">
            <label for="competitionMatch">竞争赛</label>
          </div>
        </div>


        <div class="action-container">
          <button type="button" @click="close"
                  class="button-cancel inline-flex items-center justify-center rounded-md text-sm font-medium px-4 py-2">
            Cancel
          </button>
          <button type="submit" @click="generateGame"
                  class="button-confirm inline-flex items-center justify-center rounded-md text-sm font-medium px-4 py-2">
            Save
          </button>
        </div>
      </div>
    </div>
  </div>
  <div>

    <ModalComponent v-if="isModalOpen" @close="isModalOpen = false" :playground-id="props.playgroundId"
                    @dateTimeValue="handleUpdateStartTime"
                    @runningTime="handleUpdateRunningTime">
    </ModalComponent>
  </div>

</template>
<script setup>
import {computed, getCurrentInstance, onMounted, ref} from 'vue';
import axios from "axios";
import ModalComponent from './GameDateSelectorView.vue';
import {defineProps, defineEmits} from 'vue';

const timeValue = ref('')
const isModalOpen = ref(false);
const isTimeSlotOccupied = ref(false);
const isStartTimeSelected = ref(false);
const startTime = ref('')
const runningTime = ref(1)
const gameRegistration = ref({
  gameName: '',
  gameType: 'Friendly',
});

const props = defineProps({
  someData: String,
  playgroundId: Number,

});
const selectedDuration = ref(null);
const occupiedTimeSlots = ref([]); // Example occupied slots
const emit = defineEmits(['close',]);
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getPlaygroundInfo()
});

const formattedStartTime = computed(() => {
  if (isStartTimeSelected) {
    const year = startTime.value.getFullYear();
    const month = startTime.value.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줍니다.
    const day = startTime.value.getDate();
    const hour = startTime.value.getHours();
    const minutes = startTime.value.getMinutes();

    return `${year} 年 ${month.toString().padStart(2, '0')} 月 ${day.toString().padStart(2, '0')} 日 ${hour.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
  }
  return false
});

const close = () => {
  emit('closeGameBuilder');
};

const generateGame = async () => {
  const isValid = await validate();
  if (!isValid) {
    return;
  }
  await validateAccessToken();
  try {
    const response = await axios.post(`${apiBaseUrl}/game/generate`, {
          gameName: gameRegistration.value.gameName,
          playgroundId: props.playgroundId,
          gameStartDateTime: startTime.value,
          runningTime: runningTime.value,
          gameType: gameRegistration.value.gameType,
          sportsEvent: playgroundInfo.value.sportsEvent
        }, {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
    emit('closeGameBuilder');
  } catch (error) {
    alert(error.response.data.message)
  }
};

const validate = async () => {
  if (!startTime.value) {
    alert("시간을 입력해주세요.")
    return false;
  }
  return true;
};


const playgroundInfo = ref({
  playgroundName: '',
  campusName: '',
  schoolName: '',
  sportsEvent: '',
})

const getPlaygroundInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/playground/${props.playgroundId}/info`, {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    playgroundInfo.value = response.data;
  });
};
const handleUpdateStartTime = (value) => {
  isStartTimeSelected.value = true;
  startTime.value = value;
  console.log('Received value from ModalComponent:', startTime.value);
};

const handleUpdateRunningTime = (value) => {
  runningTime.value = value;
};

const clickSelectDate = () => {
  isModalOpen.value = true;
  isTimeSlotOccupied.value = false; // Reset the occupied state when opening the modal
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
body {
  font-family: gothic, sans-serif;
}

.modal-overlay {
  position: fixed;
  top: 0;
  padding: 1px;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.event-details-container {
  min-width: 550px;
  margin: auto;
  width: 40%; /* 너비를 50%로 설정 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 5px 1px 8px 0 rgba(0, 0, 0, .06);
  border-left: 1px solid rgba(0, 0, 0, .08);;
  font-family: 'Arial', sans-serif;
  background: #fff; /* 카드의 배경색 */
  overflow: auto; /* 내용이 넘칠 때 스크롤바를 보여줌 */

}

.title {
  font-size: 27px;
  color: black;
  font-family: MiSans-Heavy, sans-serif;
}

.title-description {
  color: #949494;
  font-family: MiSans-Light, sans-serif;
  font-size: 14px;
  margin-bottom: 10px;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px; /* 각 폼 그룹 사이의 간격 */
}

.form-group label {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}

.radio-container {
  display: flex;
  align-items: start;
}

.radio-container input[type="radio"] {
  margin-right: 8px; /* 라디오 버튼과 라벨 사이의 간격 */
  cursor: pointer; /* 마우스 오버시 커서 변경 */
}

.radio-container label {
  margin-right: 20px; /* 라벨 사이의 간격 */
  cursor: pointer; /* 라벨에 마우스 오버시 커서 변경 */
}


.max-people-container input {
  width: 20%;
  padding: 12px; /* Adjust padding to match the image */
  border: 1px solid #ccc; /* Adjust border color to match the image */
  border-radius: 4px; /* Rounded corners for inputs and button */
  font-size: 14px; /* Match input font size to the image */
}

.form-group label {
  font-size: 17px; /* 레이블 글꼴 크기 */
  color: #333; /* 레이블 글꼴 색상 */
  margin-bottom: 5px; /* 레이블과 입력 필드 사이의 간격 */
}

.form-group input[type="text"] {
  padding: 12px; /* Adjust padding to match the image */
  border: 1px solid #ccc; /* Adjust border color to match the image */
  border-radius: 4px; /* Rounded corners for inputs and button */
  font-size: 12px; /* Match input font size to the image */
  font-family: MiSans-Medium, sans-serif;
}

.date-button {
  padding: 12px; /* Adjust padding to match the image */
  border: 1px solid #ccc; /* Adjust border color to match the image */
  border-radius: 4px; /* Rounded corners for inputs and button */
  font-size: 12px; /* Match input font size to the image */
  font-family: MiSans-Medium, sans-serif;
  width: 40%;
  background-color: #f5f5f5; /* Background color for the date button */
  text-align: left; /* Align text to the left */
  color: #000; /* Text color for the date button */
}


.date-button:hover {
  background-color: #ebebeb; /* Hover effect for the date button */
}


.checkbox-container input[type="checkbox"] {
  margin-right: 8px; /* Space between checkbox and label */
}

.checkbox {
  display: flex;
  margin: 5px;
}

.checkbox label {
  font-family: MiSans-Medium, sans-serif;
  color: black;
  margin: auto 0;
  font-size: 13px;
}

.action-container {
  display: flex;
  margin-top: 20px;
  justify-content: space-between;
}

.button-confirm,
.button-cancel {
  padding: 10px 20px; /* 버튼 패딩 조정 */
  border: none; /* 기본 테두리 제거 */
  font-weight: bold; /* 글꼴 굵기 */
  transition: background-color 0.2s, color 0.2s; /* 배경색과 글자색 변화 애니메이션 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

.button-confirm {
  background-color: #030000; /* 확인 버튼 배경색 */
  color: white; /* 확인 버튼 글자색 */
  border-radius: 8px;
}

.button-confirm:hover {
  background-color: #565656; /* 마우스 오버 시 배경색 어둡게 */
}

.button-cancel {
  border-radius: 8px;
  background-color: #f44336; /* 취소 버튼 배경색 */
  color: white; /* 취소 버튼 글자색 */
}

.button-cancel:hover {
  background-color: #d32f2f; /* 마우스 오버 시 배경색 어둡게 */
}

/* 날짜 선택 필드에 대한 특별한 스타일 */
input[type="date"]:valid {
  background-color: #e8f0fe;
}

/* 선택된 날짜의 스타일 */
input[type="date"]:valid::after {
  content: '✓';
  padding-left: 10px;
  color: #4CAF50;
}
</style>