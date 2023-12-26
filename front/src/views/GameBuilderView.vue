<template>
  <div class="modal-overlay">
    <div class="event-details-container">
      <h1>게임 만들기</h1>
      <h3>Please enter the details about the sporting events.</h3>

      <div class="form-container">
        <div class="form-group">
          <label for="eventName">Game Name</label>
          <input type="text" id="eventName" name="eventName" placeholder="Name of the event">
        </div>

        <div class="form-group">
          <label for="venue">Venue</label>
          <h2>{{playgroundInfo.sportsEvent}}</h2>
        </div>

        <div class="max-people-container">
          <label for="maxMatches">Maximum Number of Matches</label>
          <input type="number" id="maxMatches" name="maxMatches" placeholder="max">
        </div>

        <div class="form-group">
          <a>Event Date</a>
          <button type="button" id="eventDate" class="date-button" @click="clickSelectDate">
            {{ isStartTimeSelected ? formattedStartTime : 'Select a date' }}</button>

          <ModalComponent v-if="isModalOpen" @close="isModalOpen = false"  @updateValue="handleUpdateStartTime">
          </ModalComponent>
        </div>

        <div class="form-group checkbox-container">
          <input type="checkbox" id="friendlyMatch" name="friendlyMatch">
          <label for="friendlyMatch">Friendly Match</label>
        </div>



        <div class="action-container">
          <button type="button" class="button-cancel inline-flex items-center justify-center rounded-md text-sm font-medium px-4 py-2">Cancel</button>
          <button type="submit" class="button-confirm inline-flex items-center justify-center rounded-md text-sm font-medium px-4 py-2">Save</button>
        </div>
      </div>
    </div>
  </div>
  <div>
    <button id="eventDate" @click="clickSelectDate">select date</button>
    <ModalComponent v-if="isModalOpen" @close="isModalOpen = false"  @updateValue="handleUpdateStartTime">
    </ModalComponent>
  </div>

</template>
<script setup>
import {computed, onMounted, ref} from 'vue';
import axios from "axios";
import ModalComponent from './GameDateSelectorView.vue';
import { defineProps, defineEmits } from 'vue';

const dateValue = ref(new Date())
const timeValue = ref('')
const isModalOpen = ref(false);
const isTimeSlotOccupied = ref(false);
const isStartTimeSelected = ref(false);
const startTime = ref(new Date())
const selectedDuration = ref(null);
const occupiedTimeSlots = ref([]); // Example occupied slots
const num = ref(1)
const apiBaseUrl = "http://localhost:8080";

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

const props = defineProps({
  playgroundId: String
});


const playgroundInfo = ref({
  playgroundName: '',
  campusName: '',
  schoolName: '',
  sportsEvent: '',
})

const getPlaygroundInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/playground/2/info`,  {
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

const clickSelectDate = () => {
  isModalOpen.value = true;
  isTimeSlotOccupied.value = false; // Reset the occupied state when opening the modal
};


const pickDate = function () {
  const accessToken = localStorage.getItem("accessToken");
  axios.post(`${apiBaseUrl}game/1`, {
        year: dateValue.value.getFullYear(),
        month: dateValue.value.getMonth(),
        date: dateValue.value.getDate()
      },{  headers: {
          'Authorization': accessToken
        }}
  ).then(response => {
    occupiedTimeSlots.value = response.data;
  });
  console.log(accessToken)
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
  max-width: 50%;
  margin: auto;
  width: 50%; /* 너비를 50%로 설정 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 40px;
  box-shadow: 5px 1px 8px 0 rgba(0,0,0,.06);
  border-left: 1px solid rgba(0,0,0,.08);;
  font-family: 'Arial', sans-serif;
  background: #fff; /* 카드의 배경색 */
  overflow: auto; /* 내용이 넘칠 때 스크롤바를 보여줌 */

}

.event-details-container h1 {
  font-size: 24px;
  font-family: gothic-bold,sans-serif;
}

.event-details-container h3 {
  color: #949494;
  font-family: gothic,sans-serif;
  font-size: 12px;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px; /* 각 폼 그룹 사이의 간격 */
}

.max-people-container {
  display: flex;
  flex-direction: column;
}

.max-people-container input {
  width: 20%;
  padding: 12px; /* Adjust padding to match the image */
  border: 1px solid #ccc; /* Adjust border color to match the image */
  border-radius: 4px; /* Rounded corners for inputs and button */
  font-size: 14px; /* Match input font size to the image */
}

.form-group label {
  font-size: 14px; /* 레이블 글꼴 크기 */
  color: #333; /* 레이블 글꼴 색상 */
  margin-bottom: 5px; /* 레이블과 입력 필드 사이의 간격 */
}

.form-group input[type="text"],
.date-button {
  padding: 12px; /* Adjust padding to match the image */
  border: 1px solid #ccc; /* Adjust border color to match the image */
  border-radius: 4px; /* Rounded corners for inputs and button */
  font-size: 14px; /* Match input font size to the image */
}

.date-button {
  width: 40%;
  background-color: #f5f5f5; /* Background color for the date button */
  text-align: left; /* Align text to the left */
  color: #000; /* Text color for the date button */
}

.date-button:hover {
  background-color: #ebebeb; /* Hover effect for the date button */
}

.checkbox-container {
  flex-direction: row;
  align-items: center;
  margin-top: 0; /* Remove margin top */
}

.checkbox-container input[type="checkbox"] {
  margin-right: 8px; /* Space between checkbox and label */
}



.action-container {
  display: flex;
  margin-top: 20px;
  justify-content: space-between;
}

.button-confirm {
  margin: 0px 20px;
}

.button-cancel {
  background-color: white;
  color: black;
  margin: 0px 20px;
  border: white;
}

.button-cancel:hover {
  background-color: #f3f3f3; /* 마우스 오버 시 배경색 변경 */
  color: #000000; /* 마우스 오버 시 글자색 변경 */
}

.button-confirm:hover {
  background-color: rgba(0, 0, 0, 0.85); /* 마우스 오버 시 배경색 변경 */
  color: #ffffff; /* 마우스 오버 시 글자색 변경 */
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