<template>
  <div>
    <button @click="openModal">Open Modal</button>

    <div v-if="isModalOpen" class="modal-overlay">

      <div class="modal">

          <div class="justify-center item-center card " data-v0-t="card">
            <div class="padding-20">
              <a class="text-bold text-title primary-font">Time Selection</a>
              <h4 class="text-color-gray primary-font">Select the start time and duration for your participation.</h4>
              <div class="block">
                <el-date-picker
                    v-model="dateValue"
                    type="date"
                    placeholder="Pick a date"
                    :default-value="new Date(2010, 9, 1)"
                />
              </div>
            </div>
            <hr color="#e7e7e7">
            <div class="button-container">
              <div class="button-container">
                <div class="start-time-container">
                <a
                    class="start-time"
                    id="start-time"
                >
                  <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="15"
                      height="15"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2.2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      class="icon-style"
                  >
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="12 6 12 12 16 14"></polyline>
                  </svg>
                  Choose Start Time
                </a>
                  <el-time-picker v-model="timeValue" placeholder="Arbitrary time" />
                </div>
                <div class="start-time-container">
                  <a
                      class="start-time"
                      id="start-time"
                  >
                  <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="15"
                      height="15"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      class="icon-style"
                  >
                    <path d="M5 22h14"></path>
                    <path d="M5 2h14"></path>
                    <path d="M17 22v-4.172a2 2 0 0 0-.586-1.414L12 12l-4.414 4.414A2 2 0 0 0 7 17.828V22"></path>
                    <path d="M7 2v4.172a2 2 0 0 0 .586 1.414L12 12l4.414-4.414A2 2 0 0 0 17 6.172V2"></path>
                  </svg>
                  Choose Duration
                </a>
                  <el-input-number v-model="num" :min="1" :max="120" @change="handleChange" />
              </div>
              </div>
              <div class="container-confirm-style">
                <button class="button-cancel inline-flex items-center justify-center rounded-md text-sm font-medium  px-4 py-2"
                        @click="closeModal">
                  Cancel
                </button>
                <button class="button-confirm inline-flex items-center justify-center rounded-md text-sm font-medium px-4 py-2 ">
                  Confirm
                </button>
              </div>
            </div>
            </div>

          <div class="justify-center item-center card margin-top" data-v0-t="card">
            <div class="padding-20">
              <a class="text-bold text-title primary-font">Occupied Time Slots</a>
              <h4 class="text-color-gray primary-font">Select the start time and duration for your participation.</h4>
            </div>
            <div class="p-4 border-t">
              <div class="space-y-2">
                <div
                    v-for="(occupiedSlot, index) in occupiedTimeSlots"
                    :key="index"
                    class="time-container px-2.5 py-0.5 inline-flex items-center"
                    color="black"
                >
                  <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="15"
                      height="15"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="3"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      class="margin-right"
                  >
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="12 6 12 12 16 14"></polyline>
                  </svg>
                  {{ occupiedSlot }}
                </div>
              </div>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const dateValue = ref('')
const timeValue = ref('')
const isModalOpen = ref(false);
const isTimeSlotOccupied = ref(false);
const selectedStartTime = ref(null);
const selectedDuration = ref(null);
const occupiedTimeSlots = ref(["10:00 AM - 11:00 AM", "03:00 PM - 03:00 PM"]); // Example occupied slots
const num = ref(1)

const handleChange = (value) => {
  console.log(value);
};
const openModal = () => {
  isModalOpen.value = true;
  isTimeSlotOccupied.value = false; // Reset the occupied state when opening the modal
};

const closeModal = () => {
  isModalOpen.value = false;
};

const selectStartTime = () => {
  // Implement logic to select start time
  // For example, show a time picker or use a library like Pikaday
  selectedStartTime.value = "Selected start time";
  isTimeSlotOccupied.value = false; // Reset the occupied state when changing the start time
};

const selectDuration = () => {
  // Implement logic to select duration
  // For example, show a duration picker or use a custom input
  selectedDuration.value = "Selected duration";
};

const confirmParticipation = () => {
  // Implement logic to confirm participation
  // Check if the selected time is occupied
  if (occupiedTimeSlots.value.includes(selectedStartTime.value)) {
    isTimeSlotOccupied.value = true;
  } else {
    // Perform the action when participation is confirmed
    console.log("Participation confirmed:", selectedStartTime.value, selectedDuration.value);
    closeModal();
  }
};
</script>

<style scoped>
/* Add your modal styles here */
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

.modal {
  border: 1px #000000; /* 테두리 스타일 및 색상 지정 */
  box-shadow: 0 0 1px #bebebe;
  background: white;
  border-radius: 5px;
  width: 35vw; /* Adjust this value as needed */
  overflow-x: auto; /* Add this line to enable vertical scrolling if needed */
  height: 70vh; /* Adjust this value as needed */
  overflow-y: auto; /* Add this line to enable vertical scrolling if needed */
}

.text-bold {
  font-weight: bold;
}

.text-title {
  font-size: 24px;
}

.justify-center {
  justify-content: center;
}

svg {
  margin-bottom: 2px;
}
hr {
  color: #e7e7e7;
  margin-bottom: 10px;
}

.item-center {
  align-items: center;
}
.column {
  display: flow;
}
.text-color-gray {
  color: #838383;
}

.primary-font {
  font-family: primary-font, serif;
}

.padding-20 {
  padding: 8px;
}

.button-container {
  display: flex;
  flex-direction: column;
}

.button-style {
  background-color: #ffffff; /* 흰색 배경 */
  color: #333333; /* 글자색 */
  padding: 10px 10px; /* 내부 여백 설정 */
  width: 40%;
  border-radius: 6px;
  text-align: start;
  margin: 10px 10px;
  cursor: pointer; /* 포인터 모양 변경 */
  font-family: primary-font,serif;
  font-size: 14px;
  transition: background-color 0.3s, color 0.3s; /* 부드러운 전환 효과 */
}




.icon-style {
  display: inline-block;
  vertical-align: middle;

  margin-right: 8px; /* 아이콘과 텍스트 간격 조정 */
}

.container-confirm-style {
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

.margin-top {
  margin-top: 20px;
}

.card {
  padding: 10px;
  border: 2px #919191;/* 테두리 스타일 및 색상 지정 */
  box-shadow: 0 0 2px #b2b2b2;
  border-radius: 5px;
}

.block {
  margin-top: 15px;
  margin-bottom: 10px;
}

.time-container {
  background-color: #ffffff; /* 흰색 배경 */
  border: 1px solid #e3e3e3; /* 연한 회색 테두리 */
  border-radius: 20px;
  padding: 0 10px;
  font-size: 13px;
  margin: 5px 5px;
  font-weight: bolder;
  font-family: primary-font,sans-serif;
}

.margin-right {
  margin-right: 5px;
  margin-top: 3px;
}



.start-time-container {
  display: flex;
  background-color: #ffffff; /* 흰색 배경 */
  color: #333333; /* 글자색 */
  align-items: center;

  padding: 10px;
  cursor: pointer; /* 포인터 모양 변경 */
  transition: background-color 0.3s, color 0.3s; /* 부드러운 전환 효과 */
}

.start-time {
  text-align: center;
  padding-top: 3px;
  margin: 0 19px 0px 12px;
}
</style>