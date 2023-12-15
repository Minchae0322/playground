<template>
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

const isModalVisible = ref(true);
const selectedTeam = ref({ name: '' });
const dropdownVisible = ref(false);
const apiBaseUrl = "http://localhost:8080";

const router = useRouter();
const teams = ref([])
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
      const newAccessToken = response.headers['Authorization'];
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
  console.log('Confirmed team:', selectedTeam.value.name);
};
</script>

<style scoped>
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