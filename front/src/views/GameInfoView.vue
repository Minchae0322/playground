<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";

import {useRouter} from "vue-router";


const router = useRouter();
const homeTeams = ref([])
const awayTeams = ref([])
const homeAndAwayTeam = ref([]);
const homeAndAwayTeamParams = ref({
      homeAndAwayTeamTitle: "Home"
    }
)
const apiBaseUrl = "http://localhost:8080";
const isTeamRegistrationModalVisible = ref(false);
const selectedTeam = ref("");
const dropdownVisible = ref(false);
const menuVisible = ref(false)
const teams = ref([])
const activeName = ref('first')
const props = defineProps({
  gameId: {
    type: Number,
    required:true,
  }
})

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getHomeTeams()
  getAwayTeams()

});

const joinGameSolo = () => {
  validateAccessToken()
  axios.post(`${apiBaseUrl}/game/${props.gameId}/join/SoloGameJoin`,{
        matchTeamSide: "HOME"
  },
        {  headers: {
            'Authorization': getAccessToken()
          }}
    ).then(response => {
      if (response.status === 200) {
      }
    });
};

const clickTeamRegistration = () => {
  isTeamRegistrationModalVisible.value = true;
};

const clickHomeTeam = () => {
  activeName.value = 'first'
  homeAndAwayTeam.value = homeTeams.value;
  homeAndAwayTeamParams.value.homeAndAwayTeamTitle = "Home"
};

const clickAwayTeam = () => {
  activeName.value = 'second'
  homeAndAwayTeam.value = awayTeams.value;
  homeAndAwayTeamParams.value.homeAndAwayTeamTitle = "Away"
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

const handleClick = (tab, event) => {
  console.log(tab.name)
  console.log(event.value)
  if(tab.name === 'first') {
    clickHomeTeam();
  } else if(tab.name === 'second') {
    clickAwayTeam();
  }
};
const redirectToLogin = function () {
  router.push("/login");
};
const getHomeTeams = function () {
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.get(`${apiBaseUrl}/game/${props.gameId}/homeTeams`,
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

const toggleMenu = function () {
  menuVisible.value = !menuVisible.value;
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
  isTeamRegistrationModalVisible.value = false;
  // 추가 취소 로직
};

const confirm = () => {
  isTeamRegistrationModalVisible.value = false;
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
    axios.get(`${apiBaseUrl}/game/${props.gameId}/awayTeams`,
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


<template>
  <div class="game-information">
    <div class="tab-container">
      <div
          role="tablist"
          aria-orientation="horizontal"
          class="tab-list"
          tabindex="0"
      >
        <button
            type="button"
            role="tab"

            class="tab-button"
            :data-state="activeName === 'first' ? 'active' : 'inactive'"
            :class="{ active: activeName === 'first' }"
            @click="clickHomeTeam"
            tabindex="0"
        >
          Home Team
        </button>
        <button
            type="button"
            role="tab"
            :aria-selected="activeName === 'second'"
            aria-controls="radix-:r0:-content-awayTeam"
            class="tab-button"
            :data-state="activeName === 'second' ? 'active' : 'inactive'"
            :class="{ active: activeName === 'second' }"
            @click="clickAwayTeam"
            tabindex="0"
        >
          Away Team
        </button>
      </div>


  </div>

    <div class="teams-container">
      <div class="team">
        <h2>{{ homeAndAwayTeamParams.homeAndAwayTeamTitle }}</h2>
        <div class="team-details" v-for="(team, index) in homeAndAwayTeam" :key="index">
          <h2>{{ team.teamName }}</h2>
          <div class="participants-container">
            <div class="participant" v-for="(participant, index) in team.users" :key="index">
              <span class="user-id">{{ participant.userId }}</span>
              <span class="user-nickname">{{ participant.userNickname }}</span>
            </div>
          </div>
          <button class="add-button" @click="addParticipant(team.teamId)">Add Player</button>
        </div>

          <div id="app" class="flex justify-center items-center h-screen">
            <div class="relative">
              <button
                  class="styled-button"
                  @click="toggleMenu"
                  aria-haspopup="menu"
                  :aria-expanded="menuVisible.toString()"
              >
                +
              </button>
              <div v-if="menuVisible" class="py-1" role="menu" aria-orientation="vertical" aria-labelledby="options-menu">
                <a  @click="joinGameSolo" class="menu-item" role="menuitem">개인으로 참가하기</a>
                <a @click="clickTeamRegistration"  class="menu-item" role="menuitem">팀으로 참가하기</a>
              </div>
            </div>
          </div>

      </div>


    </div>

    <!-- Button for more details -->
  </div>

  <div v-if="isTeamRegistrationModalVisible" class="modal-overlay">
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


<style scoped>
.game-information {
  width: 90%;
  font-family: 'Arial', sans-serif;
  color: #333;
  text-align: center;

}

.tab-container {
  display: flex; /* Flexbox를 활성화 */
  justify-content: center; /* 수평 방향 가운데 정렬 */
  align-items: center; /* 수직 방향 가운데 정렬 */
  width: 70%; /* teams-container와 동일한 너비 */
  margin: auto; /* 가운데 정렬을 위해 자동 마진 사용 */
  height: auto; /* 컨텐츠에 맞게 높이 설정 */
}

.tab-list {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 3rem; /* 세로 길이 증가 */
  background-color: #f0f0f0; /* muted background */
  padding: 0.5rem; /* 패딩 증가 */
  border-radius: 0.875rem; /* lg */
  width: 100%;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* teams-container 스타일과 일치 */
}


.tab-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
  border-radius: 0.375rem; /* md */
  padding: 0.75rem; /* 3 */
  font-size: 0.875rem; /* sm */
  font-weight: 500; /* medium */
  transition: all 0.3s ease;
  cursor: pointer;
  border: none; /* Remove border */
  background-color: transparent; /* Inactive state background */
  color: #888888; /* muted-foreground */
  outline: none;
}

.tab-button.active {
  background-color: #ffffff; /* active background */
  color: #000000; /* text-foreground */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* active shadow */
}

.tab-button:focus-visible {
  ring-width: 2px;
  ring-color: #bbbbbb; /* ring */
  ring-offset-width: 2px;
  ring-offset-color: #ffffff; /* ring-offset-background */
}

.tab-button:disabled {
  pointer-events: none;
  opacity: 0.5;
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
  justify-content: center;

  margin-top: 20px;
}

.team {
  border: 2px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  text-align: left;
  width: calc(70% - 40px); /* Adjust for padding and spacing between teams */
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

.styled-button {
  padding: 8px 16px;
  border: 1px solid #ccc;
  width: 90%;
  border-radius: 4px;
  margin-bottom: 5px; /* 필요에 따라 조정 */
  font-size: 14px;
  cursor: pointer;
  outline: none;

  z-index: 1000; /* 메뉴가 버튼 아래에 오도록 z-index 설정 */
}

.styled-button:hover {
  background-color: #f8f8f8;
}

.styled-menu {
  position: absolute;
  top: 100%; /* 버튼 바로 아래에 위치 */
  left: 0;
  border: 1px solid #ccc;
  border-radius: 4px;

  width: max-content;
  z-index: 1000;
}

.menu-item {
  display: block;
  padding: 8px 16px;
  border-bottom: 1px solid #ccc; /* 마지막 메뉴 아이템을 제외한 각 메뉴 아이템 사이의 구분선 */
  color: #333;
  text-decoration: none;
}

.menu-item:last-child {
  border-bottom: none; /* 마지막 메뉴 아이템의 구분선 제거 */
}

.menu-item:hover {
  background-color: #f8f8f8;
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




.styled-select {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 90%; /* 셀렉트 너비 설정 */
  height: 40px; /* 셀렉트 높이 설정 */
  padding: 5px 10px; /* 내부 여백 설정 */
  font-size: 16px; /* 글자 크기 설정 */
  border: 2px solid #ddd; /* 테두리 설정 */
  border-radius: 5px; /* 테두리 둥글기 설정 */
  appearance: none; /* 기본 브라우저 스타일 제거 */
  background-color: #fff; /* 배경색 설정 */
  cursor: pointer; /* 커서 모양 설정 */
}

/* 화살표 대신 사용할 배경 이미지 설정 */


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

a {
  font-family: gothic-bold;
}
</style>