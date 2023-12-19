<template>
  <div class="game-information">
    <h1>Game Information</h1>
    <p>Details about the upcoming match.</p>

    <div class="teams-container">
      <div class="team">
        <h2>Home</h2>
        <div class="team-details" v-for="(team, index) in homeTeams" :key="index">
          <h2>{{ team.teamName }}</h2>
          <div class="participants-container">
            <div class="participant" v-for="(participant, index) in team.users" :key="index">
              <span class="user-id">{{ participant.userId }}</span>
              <span class="user-nickname">{{ participant.userNickname }}</span>
            </div>
          </div>
          <button class="add-button" @click="addParticipant(team.teamId)">Add Player</button>
        </div>
        <button class="add-button" @click="visibleModal"></button>

      </div>

      <div class="team">
        <h2>Away</h2>
        <div class="team-details" v-for="(team,index) in awayTeams" :key="index">
          <!--          <p>{{ team.teamId }}</p>-->
          <h2>{{ team.teamName }}</h2>
          <div class="participant" v-for="(participant, index) in team.users" :key="index">
            <p>{{ participant.userId }}</p>
            <p>{{ participant.userNickname }}</p>
          </div>
          <button class="add-button" @click="addParticipant()">Add</button>
        </div>
      </div>
    </div>

    <!-- Button for more details -->
  </div>

  <div v-if="isModalVisible" class="modal-overlay">
    <div class="modal-window">
      <h2>Select Your Team</h2>
      <p>Choose a team from the dropdown list.</p>
      <div class="dropdown">
        <div class="dropdown-selected" @click="toggleDropdown">{{ selectedTeam.name || 'Select a team' }}</div>
        <div class="dropdown-content" v-show="dropdownVisible">
          <div class="dropdown-item" v-for="team in teams" :key="team.id" @click="selectTeam(team)">

            <span class="team-name">{{ team.teamName }}</span>
            <span class="team-description">{{ team.description }}</span>
          </div>
        </div>
      </div>
      <div>
        <span>Selected Team:</span>
        <span>{{ selectedTeam.teamName }}</span>
      </div>
      <button @click="cancel">Cancel</button>
      <button @click="confirm">Confirm</button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";

import {useRouter} from "vue-router";

const router = useRouter();
const homeTeams = ref([])
const awayTeams = ref([])
const apiBaseUrl = "http://localhost:8080";
const isModalVisible = ref(false);
const selectedTeam = ref("");
const dropdownVisible = ref(false);



const teams = ref([])

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getHomeTeams()
  getAwayTeams()
});

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
const getHomeTeams = function () {
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.get(`${apiBaseUrl}/game/34/homeTeams`,
        {  headers: {
            'Authorization': accessToken
          }}
    ).then(response => {
      if (response.status === 200) {
        homeTeams.value = response.data
      }
    });
  }
};

const getTeamsUserBelongTo = function () {
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.get(`${apiBaseUrl}/user/teams`,
        {  headers: {
            'Authorization': accessToken
          }}
    ).then(response => {
      if (response.status === 200) {
        teams.value = response.data
      }
    });
  }
};

const visibleModal = function () {
  isModalVisible.value = true;
};
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
  getTeamsUserBelongTo()
};

const selectTeam = (team) => {
  selectedTeam.value = team;
  dropdownVisible.value = false;
};

const cancel = () => {
  isModalVisible.value = false;
  // 추가 취소 로직
};

const confirm = () => {
  isModalVisible.value = false;
  // 선택된 팀을 처리하는 로직
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.post(`${apiBaseUrl}/game/34/request/create-team`,
        { teamId: 1 }, {
          headers: {
            'Authorization': accessToken,
          }}
    ).then(response => {

    });
  }
};

const getAwayTeams = function () {
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.get(`${apiBaseUrl}/game/34/awayTeams`,
        {  headers: {
            'Authorization': accessToken
          }}
    ).then(response => {
      if (response.status === 200) {
        awayTeams.value = response.data
      }
    });
  }
};


const selectedTeamId = ref(null);
const participantType = ref('');

const showSelect = () => {
  selectedTeamId.value = teamId; // Show the select box

};

const handleSelection = (type, teamId) => {
  // Handle the selection logic here
  console.log(`Selected: ${type} for team ID: ${teamId}`);
  // Reset after handling
  selectedTeamId.value = null;
};
</script>

<style scoped>
.game-information {
  width: 90%;
  margin: 20px auto;
  font-family: 'Arial', sans-serif;
  color: #333;
  text-align: center;
}

h1 {
  font-size: 24px;
  color: #444;
}

p {
  font-size: 16px;
  color: #666;
}

.teams-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.team {
  border: 2px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  text-align: left;
  width: calc(50% - 40px); /* Adjust for padding and spacing between teams */
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  background: #fff;
}

.team h2 {
  font-size: 26px;
  font-weight: bold;
  font-family: primary-font,sans-serif;
  color: #000000;
}

.team-details {
  background: #F3F4F6; /* Light grey background */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow */
  padding: 10px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* Align items to the start of the flex container */
}

.team-details h2 {
  font-size: 20px; /* Smaller font size for team name */
  color: #333; /* Dark grey color for text */
  margin-bottom: 10px; /* Space below the team name */
}

.team-details p {
  font-size: 16px; /* Smaller font size for details */
  color: #666; /* Light grey color for text */
  margin-bottom: 5px; /* Reduce space between details */
}

.add-button {
  background-color: #007BFF; /* Bootstrap blue */
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 4px; /* Rounded corners for the button */
  margin-top: 10px; /* Space above the button */
  align-self: center; /* Center the button in the flex container */
}

.add-button::before {
  content: none; /* Remove the plus sign since the button text will be 'Add Player' */
}

.team-details:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.select-container {
  text-align: center;
  margin-top: 10px;
}

.select-container select {
  padding: 10px;
  margin-right: 5px;
}

.participant {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 10px;
  margin: 10px 0;
  transition: transform 0.3s ease, background-color 0.3s ease;
}

.participant:hover {
  transform: translateY(-3px);
  background-color: #ececec;
}

.participant p {
  margin: 0;
  font-size: 14px;
  color: #444;
}
.participants-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px; /* Creates a gap between participant items */
  margin-bottom: 15px;
}

.participant {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px 16px;
  transition: background-color 0.3s ease;
}

.participant:hover {
  background-color: #f8f8f8;
}

.user-id {
  font-weight: bold;
  margin-right: 5px;
}

.user-nickname {
  color: #888;
}

.add-button {
  /* ... existing add-button styles ... */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-button:hover {
  /* ... existing hover effect or create a new one ... */
  background-color: #0056b3;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}


/* Media query for responsiveness */
@media (max-width: 768px) {
  .teams-container {
    flex-direction: column;
  }

  .team {
    width: 100%;
    margin-bottom: 20px;
  }

  .game-information {
    width: 95%;
  }
}

.add-button {
  background-color: rgba(70, 130, 180, 0.3); /* Semi-transparent blue color */
  color: white;
  border: 2px solid rgba(70, 130, 180, 0.9); /* Solid blue border for contrast */
  border-radius: 6px; /* Makes the button round */
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 90%;

  position: relative; /* Needed to position the pseudo-element */
}

.add-button::before {
  content: "+"; /* Adds the plus sign */
  font-size: 24px; /* Size of the plus sign */

}

.add-button:hover {
  background-color: rgba(70, 130, 180, 0.5); /* Darker transparent blue on hover */
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-window {
  background: white;
  padding: 30px;
  width: 300px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.modal-window h2 {
  margin-bottom: 15px;
  font-size: 1.5em;
}

.modal-window p {
  margin-bottom: 20px;
}

.dropdown {
  position: relative;
  margin-bottom: 20px;
}

.dropdown-selected {
  padding: 10px 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  cursor: pointer;
  text-align: left;
  margin-bottom: 10px;
}

.dropdown-content {
  display: block;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content .dropdown-item {
  padding: 12px 16px;
  cursor: pointer;
}

.dropdown-content .dropdown-item:hover {
  background-color: #f1f1f1;
}

.team-image {
  width: 20px;
  height: auto;
  margin-right: 10px;
  vertical-align: middle;
}

.team-name {
  font-weight: bold;
  vertical-align: middle;
}

.team-description {
  display: block;
  font-size: 0.8em;
  margin-top: 5px;
}

.selected-team-display {
  padding: 10px;
  background-color: #f2f2f2;
  border-radius: 5px;
  display: inline-block;
  margin-bottom: 20px;
}

button {
  padding: 10px 20px;
  margin: 5px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 0.9em;
  font-weight: bold;
  width: 100px; /* 버튼 너비를 고정 */
}

button:hover {
  opacity: 0.8;
}

.cancel-button {
  background: #fff;
  color: #333;
  border: 1px solid #ccc;
}

.confirm-button {
  background: #000;
  color: #fff;
}

/* 추가적으로 선택된 팀을 표시하는 요소에 대한 스타일 */
.selected-team {
  font-size: 1em;
  font-weight: bold;
  color: #333;
  padding: 8px 16px;
  background-color: #e9e9e9;
  border-radius: 5px;
  display: inline-block;
  margin-top: 10px;
}
</style>