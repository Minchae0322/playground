<template>
  <div class="modal-overlay">
<div class="event-details-container">
<h1>게임 만들기</h1>
<h3>Please enter the details about the sporting events.</h3>
<div>
  <button id="eventDate" @click="clickSelectDate">select date</button>
  <ModalComponent v-if="isModalOpen" @close="isModalOpen = false"  @updateValue="handleUpdateValue">
  </ModalComponent>
</div>
<form>
  <label for="eventName">Event Name</label>
  <input type="text" id="eventName" name="eventName" placeholder="Name of the event">

  <label for="venue">Venue</label>
  <input type="text" id="venue" name="venue" placeholder="Venue of the event">

  <label for="maxMatches">Maximum Number of Matches</label>
  <input type="number" id="maxMatches" name="maxMatches" placeholder="Enter the number">

  <div class="checkbox-container">
    <input type="checkbox" id="friendlyMatch" name="friendlyMatch">
    <label for="friendlyMatch">Friendly Match</label>
  </div>

  <label for="eventDate">Event Date</label>



  <div class="form-actions">
    <button type="button" class="cancel-button">Cancel</button>
    <button type="submit" class="save-button">Save</button>

  </div>
</form>


</div>

  </div>
</template>
<script setup>
import { ref } from 'vue';
import axios from "axios";
import ModalComponent from './GameDateSelectorView.vue';
import { defineProps, defineEmits } from 'vue';

const dateValue = ref(new Date())
const timeValue = ref('')
const isModalOpen = ref(false);
const isTimeSlotOccupied = ref(false);
const selectedStartTime = ref(null);
const selectedDuration = ref(null);
const occupiedTimeSlots = ref([]); // Example occupied slots
const num = ref(1)
const apiBaseUrl = "http://localhost:8080/";

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
  axios.get(`${apiBaseUrl}/playground/${props.playgroundId}/info`, {}, {
        headers: {
          'Authorization': localStorage.getItem("refreshToken")
        }
      }
  ).then(response => {
    playgroundInfo.value = response.data;
  });
};
const handleUpdateValue = (value) => {
  receivedValue.value = value;
  isModalOpen.value = false;
  console.log('Received value from ModalComponent:', receivedValue.value);
};
const receivedValue = ref('');
const clickSelectDate = () => {
  isModalOpen.value = true;
  isTimeSlotOccupied.value = false; // Reset the occupied state when opening the modal
};

const closeModal = () => {
  isModalOpen.value = false;
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

.event-details-container {
  width: 300px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;

  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

}

.event-details-container h1 {
  font-size: 24px;
  font-family: gothic-bold,sans-serif;
  text-align: center;
}

.event-details-container h3 {
  text-align: center;
  color: #949494;
  font-family: gothic,sans-serif;
  font-size: 12px;
}

form {
  display: flex;
  flex-direction: column;
}

form label {
  margin-top: 10px;
}

form input[type="text"],
form input[type="number"]{
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box; /* Makes sure padding does not affect the width */
}

.checkbox-container {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.checkbox-container label {
  margin-left: 5px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.cancel-button,
.save-button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button {
  background: #f5f5f5;
}

.save-button {
  background: #064183;
  color: white;
}

button:hover {
  opacity: 0.8;
}

</style>