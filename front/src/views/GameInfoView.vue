<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import { defineEmits } from 'vue';
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";
import { defineProps } from 'vue';

const apiBaseUrl = "http://localhost:8080";

const router = useRouter();
const homeTeams = ref({
  matchTeamSide: "HOME",
  subTeams: [{
    teamProfileImg: '',
    users: [{
      userProfileImg:''
    }]
  }],
  soloTeam: {
    users: [{
      userProfileImg:''
    }]
  }
})
const awayTeams = ref({
  matchTeamSide: "AWAY",
  subTeams: [{
    teamProfileImg: '',
    users: [{
      userProfileImg:''
    }]
  }],
  soloTeam: {
    users: [{
      userProfileImg:''
    }]
  }
})
const homeAndAwayTeams = ref({});

const isTeamRegistrationModalVisible = ref(false);
const teamsUserBelongTo = ref([{
  teamId: '',
  teamProfileImg: '',
  teamName: '',
}])
const selectedTeam = ref({});
const dropdownVisible = ref(false);

const menuVisible = ref(false)
const activeName = ref('first')
const props = defineProps({
  game: {
    type: Object,
    required: true
  }
});


const emits = defineEmits(['goBack']);

function goBack() {
  emits('goBack'); // 부모 컴포넌트에게 뒤로가기 이벤트 전달
}

onMounted(async () => {
  await getTeamData('HOME')
  await getTeamData('AWAY')
  await clickHomeTeam()
});


function encodeImageToBase64(image) {
  return image ? `data:image/jpeg;base64,${image}` : '';
}

// 사용자 정보를 변환하는 함수
function transformUsers(users) {
  return users.map(user => ({
    ...user,
    userProfileImg: encodeImageToBase64(user.userProfileImg)
  }));
}

// 팀 데이터를 업데이트하는 함수
const getTeamData = async (matchTeamSide) => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/game/${props.game.gameId}/${matchTeamSide}`, {
      headers: {
        'Authorization': getAccessToken()
      }
    });

    if (response.status === 200) {
      const transformedSubTeams = response.data.subTeams.map(subTeam => ({
        ...subTeam,
        teamProfileImg: encodeImageToBase64(subTeam.teamProfileImg),
        users: transformUsers(subTeam.users)
      }));

      const transformedSoloTeamUsers = transformUsers(response.data.soloTeam.users);

      if (matchTeamSide === 'HOME') {
        homeTeams.value = { ...homeTeams.value, subTeams: transformedSubTeams, soloTeam: { users: transformedSoloTeamUsers } };
      } else if (matchTeamSide === 'AWAY') {
        awayTeams.value = { ...awayTeams.value, subTeams: transformedSubTeams, soloTeam: { users: transformedSoloTeamUsers } };
      }
    }
  } catch (error) {
    console.error(`Failed to fetch ${matchTeamSide} teams data:`, error);
    // 에러 처리 로직을 추가할 수 있습니다.
  }
};
const sendSoloGameJoinRequest = async () => {
  await validateAccessToken()
  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/soloGameJoin`,{
          matchTeamSide: homeAndAwayTeams.value.matchTeamSide,
        },
        {  headers: {
            'Authorization': getAccessToken()
          }}
    )
  } catch (error) {
    showErrorMessage(error)
  }
};

const sendTeamRegistrationRequest = async () => {
  if (!selectedTeam.value.teamId) {
    alert("팀을 선택해주세요");
    return;
  }

  isTeamRegistrationModalVisible.value = false;

  await validateAccessToken();

  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/teamGameRegistration`, {
      teamId: selectedTeam.value.teamId,
      matchTeamSide: homeAndAwayTeams.value.matchTeamSide
    }, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    // 서버에서 성공 응답을 받았을 때의 처리를 이곳에 추가할 수 있습니다.
  } catch (error) {
    showErrorMessage(error)
  }
};

const sendTeamJoinRequest = async (subTeamId) => {
  await validateAccessToken();
  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/teamGameJoin`,
        {
          subTeamId: subTeamId,
          matchTeamSide: homeAndAwayTeams.value.matchTeamSide
        }, {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    )
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

const clickTeamRegistration = () => {
  isTeamRegistrationModalVisible.value = true;
};

const clickHomeTeam = () => {
  activeName.value = 'first'
  homeAndAwayTeams.value = homeTeams.value;

};

const clickAwayTeam = () => {
  activeName.value = 'second'
  homeAndAwayTeams.value = awayTeams.value;

};

const toggleJoinMenu = function () {
  menuVisible.value = !menuVisible.value;
};

const toggleUserTeamDropdown = () => {
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
const getTeamsUserBelongTo = function () {
  validateAccessToken();
  axios.get(`${apiBaseUrl}/user/teams`,
      {  headers: {
          'Authorization': getAccessToken()
        }}
  ).then(response => {
    teamsUserBelongTo.value = response.data.map(team => ({
      teamName: team.teamName,
      teamSportsEvent: team.sportsEvent,
      teamId: team.teamId,
      teamProfileImg: team.teamProfileImg ? `data:image/jpeg;base64,${team.teamProfileImg}` : defaultImage
    }));
  });
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


<template>
  <div class="game-information">
    <button @click="goBack">뒤로 가기</button>
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
        <h2>{{ homeAndAwayTeams.matchTeamSide}}</h2>
        <div v-if="homeAndAwayTeams.subTeams">
        <div class="team-details" v-for="(team, index) in homeAndAwayTeams.subTeams" :key="index">
          <img class="team-image" :src="team.teamProfileImg || defaultImage">
          <h2>{{ team.teamName }}</h2>
          <div class="participants-container">
            <div class="participant" v-for="(participant, index) in team.users" :key="index">
              <img :src="participant.userProfileImg || defaultImage">
              <span class="user-nickname">{{ participant.userNickname }}</span>
            </div>
          </div>
          <button class="add-button" @click="sendTeamJoinRequest(team.subTeamId)">Add Player</button>
        </div>
        </div>
        <div v-if="homeAndAwayTeams.soloTeam">
        <div class="team-details" v-for="(participant, index) in homeAndAwayTeams.soloTeam.users" :key="index">
          <div class="participant">
            <img :src="participant.userProfileImg || defaultImage">
            <span class="user-nickname">{{ participant.userNickname }}</span>
          </div>

        </div>
        </div>


          <div id="app" class="flex justify-center items-center h-screen">
            <div class="relative">
              <button
                  class="styled-button"
                  @click="toggleJoinMenu"
                  aria-haspopup="menu"
                  :aria-expanded="menuVisible.toString()"
              >
                +
              </button>
              <div v-if="menuVisible" class="py-1" role="menu" aria-orientation="vertical" aria-labelledby="options-menu">
                <a  @click="sendSoloGameJoinRequest" class="menu-item" role="menuitem">개인으로 참가하기</a>
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
      <h2>팀 만들기</h2>
      <p>경기에 참여 할 팀을 만들고 참여합니다.</p>
      <div class="dropdown">
        <div class="dropdown-selected" @click="toggleUserTeamDropdown">{{ selectedTeam.name || 'Select a team' }}</div>
        <div class="dropdown-content" v-show="dropdownVisible">
          <div class="dropdown-item" v-for="team in teamsUserBelongTo" :key="team.id" @click="selectTeam(team)">
            <img :src="team.teamProfileImg || defaultImage" class="team-image">
            <a class="team-name">{{ team.teamName }}</a>
            <a class="team-description">{{ team.description }}</a>
          </div>
        </div>
      </div>
      <div class="selected-team-container">
        <a>선택한 팀:</a>
        <img :src="selectedTeam.teamProfileImg || defaultImage" class="team-image">
        <a>{{ selectedTeam.teamName }}</a>
      </div>
      <div class="container-confirm-style">
      <button @click="cancel" class="button-cancel inline-flex items-center justify-center rounded-md text-sm font-medium  px-4 py-2">Cancel</button>
      <button @click="sendTeamRegistrationRequest" class="button-confirm inline-flex items-center justify-center rounded-md text-sm font-medium  px-4 py-2">Confirm</button>
      </div>
    </div>
  </div>
</template>


<style scoped>
h1 {
  font-size: 24px;
  color: #838383;
}

p {
  font-size: 11px;
  color: #838383;
}

h2 {
  font-family: gothic-bold;
}

button:hover {
  opacity: 0.8;
}

a {
  font-family: gothic-bold;
  font-size: 17px;
}

.game-information {
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



.teams-container {
  display: flex;
  justify-content: center; /* 가로 방향 가운데 정렬 */
  align-items: center; /* 세로 방향 가운데 정렬 */
  margin-top: 20px;
  width: 100%; /* 컨테이너 너비를 100%로 설정 */
}

.team {
  border: 2px solid #ddd;
  border-radius: 8px;
  padding: 20px;

  margin: 0 auto; /* .team 요소를 가로 방향으로 가운데 정렬 */
  width: calc(90% - 40px); /* 여기서는 .team 요소의 너비를 90%로 설정, 여백이나 패딩을 고려하여 조정 */
  /* 기타 스타일 */
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  background: #fff;
}

.team h2 {
  font-size: 26px;
  font-weight: bold;
  text-align: left;
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
  background-color: rgba(0, 0, 0, 0.6); /* 약간 더 어둡게 조정 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-window {
  background: white;
  padding: 20px; /* 패딩 조정 */
  border-radius: 5px; /* 모서리 둥글기 조정 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); /* 그림자를 더 부드럽게 조정 */
  width: 40%; /* 너비를 내용물에 맞게 자동으로 조정 */
  max-width: 50%; /* 최대 너비 설정 */
}

.modal-window h2 {
  color: #333; /* 텍스트 색상 */
  font-size: 1.25rem; /* 제목 글자 크기 */
  margin-top: 0; /* 상단 여백 제거 */
  margin-bottom: 0.5rem; /* 하단 여백 조정 */
}

.modal-window p {
  color: #838383; /* 텍스트 색상 */
  font-size: 0.8rem; /* 본문 글자 크기 */
  margin-top: 0; /* 상단 여백 제거 */
  margin-bottom: 1.5rem; /* 하단 여백 조정 */
}


.styled-select select {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.25rem;
  height: 2.5rem; /* 드롭다운 높이 조정 */
  -webkit-appearance: none; /* 기본 디자인 제거 */
  -moz-appearance: none;
  appearance: none;
  background-color: #fff;
  padding-right: 2.5rem; /* 오른쪽 화살표 공간 확보 */
}

.styled-select::after {
  content: "▼";
  position: absolute;
  top: 50%;
  right: 1rem;
  transform: translateY(-50%);
  pointer-events: none; /* 클릭 이벤트가 드롭다운을 통과하도록 설정 */
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

.dropdown-content .dropdown-item img {
  float: left; /* 이미지를 왼쪽으로 정렬합니다. */
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
  display: flex; /* Flex container로 설정 */
  align-items: center; /* 세로축 중앙 정렬 */
  padding: 12px 16px;
  cursor: pointer;
}


/* Team Name Style */
.team-name {
  font-weight: bold;
  margin-left: 10px; /* 이름과 이미지 사이 여백 */
}



.dropdown-content .dropdown-item:hover {
  background-color: #f1f1f1;

}

.team-name {
  font-weight: bold;

}

.team-description {
  display: block;
  font-size: 0.8em;
  margin-top: 5px;
}

.selected-team-container {
  display: flex;
  align-items: center;
  margin: 20px 0;
}

.selected-team-container img {
  width: 30px;
  height: 30px;
}

.selected-team-container a {
  margin-right: 10px;
}



.team-image {
  width: 50px; /* 이미지 크기 조절 */
  height: 50px; /* 이미지 크기 조절 */
  border-radius: 25%; /* 이미지를 원형으로 만듦 */
  object-fit: cover; /* 이미지 비율을 유지하면서 요소에 맞게 조정 */
  border: 2px solid #ddd; /* 이미지 주변에 테두리 추가 */
  margin-right: 10px; /* 텍스트와의 간격을 위한 마진 */
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 이미지에 그림자 추가 */
}

/* User Profile Image Style */
.participant img {
  width: 40px; /* 이미지 크기 조절 */
  height: 40px; /* 이미지 크기 조절 */
  border-radius: 50%; /* 이미지를 원형으로 만듦 */
  object-fit: cover; /* 이미지 비율을 유지하면서 요소에 맞게 조정 */
  border: 1px solid #ccc; /* 이미지 주변에 테두리 추가 */
  margin-right: 10px; /* 닉네임과의 간격을 위한 마진 */
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1); /* 이미지에 그림자 추가 */
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

button.goBack {
  position: absolute;
  top: 20px;
  left: 20px;
  padding: 10px 20px;
  background-color: #f8f8f8;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button.goBack:hover {
  background-color: #e2e2e2;
}
</style>