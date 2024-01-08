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
const homeAndAwayTeams = ref({
  matchTeamSide: "HOME",
});

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
  <div class="game-information-container">
    <div class="game-info">
      <h2>{{ homeAndAwayTeams.matchTeamSide}}</h2>
      <div class="tab-container">
        <button class="button-goBack" @click="goBack">Back</button>
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
            主队 <span class="checkmark" v-show="activeName === 'first'">✔</span>
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
          >客队 <span class="checkmark" v-show="activeName === 'second'">✔</span>
          </button>
        </div>
    </div>

  </div>


    <div class="teams-container">
      <div class="team">

        <div v-if="homeAndAwayTeams.subTeams">
          <div class="team-details" v-for="(team, index) in homeAndAwayTeams.subTeams" :key="index">
            <div class="team-info-container">
              <img class="team-image" :src="team.teamProfileImg || defaultImage">
              <div class="team-info">
                <div class="team-name">{{ team.teamName }}</div>
                <div class="team-description">{{ team.teamDescription }}</div>
              </div>

            </div>
            <div class="team-member-container">
              <div class="team-member" v-for="(participant, index) in team.users" :key="index">
                <img class="team-member-photo" :src="participant.userProfileImg || defaultImage">
                <p class="user-nickname">{{ participant.userNickname }}</p>
                <p class="user-role">{{ participant.userRole }}</p>
              </div>
            </div>
            <button class="add-button" @click="sendTeamJoinRequest(team.subTeamId)"> 参加</button>
          </div>
        </div>
        <div  v-if="homeAndAwayTeams.soloTeam">
        <div class="team-details" v-for="(participant, index) in homeAndAwayTeams.soloTeam.users" :key="index">
          <div class="team-member">
            <img :src="participant.userProfileImg || defaultImage">
            <p class="user-nickname">{{ participant.userNickname }}</p>

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


body {
  background: var(--white);
}

h1 {
  background-color: #333;
  padding: 10px;
  text-align: center;
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

/* 게임 정보 컨테이너 및 기본 스타일 */
.game-information-container {
  font-family: 'Arial', sans-serif;
  text-align: center;
  background: var(--white);
}

.game-info {
  width: 100%;

  background: var(--white);
}

.game-info h2 {
  background-image: linear-gradient(to right, #6a85b6 0%, #bac8e0 100%);
  color: white;
  padding: 10px 10px 10px 20px;
  font-size: 130%;
  font-weight: bold;
  width: 100%;
  text-align: left;
  font-family: primary-font,sans-serif;
}
/* 뒤로 가기 버튼 스타일 */


/* 탭 컨테이너 스타일 */
.tab-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: var(--white);
  width: 100%;
  margin: auto;
  height: auto;
}

.button-goBack {
  background: var(--accent-color);
  border: 1px solid rgba(50, 58, 65, 0.9); /* Solid blue border for contrast */
  padding: 10px 20px; /* 버튼 패딩 조정 */

  color: white; /* 텍스트 색상 */
  border-radius: 4px;
  margin: 10px auto 10px 25px;
  text-align: left; /* 왼쪽 정렬 */
}

/* 탭 리스트 스타일 */
.tab-list {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: var(--background-color);
  border-radius: 4px;
  width: 95%;

}

/* 탭 버튼 기본 스타일 */
.tab-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
  border-radius: 4px;
  font-size: 17px;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;;
  padding: 10px;
  cursor: pointer;
  width: 50%;
  border: 1px solid #d0d0d0;
  background-color: transparent;
  color: #989898;
  outline: none;
}

.checkmark {
  display: none; /* 기본적으로 보이지 않도록 설정 */
  margin-left: 10px; /* 텍스트로부터 여백 */
  color: var(--secondary-color); /* 체크 표시 색상 */
  font-size: 18px; /* 체크 표시 크기 */
}

/* 탭 버튼 활성화 시 체크 표시 보이게 하기 */
.tab-button.active .checkmark {
  display: inline; /* 활성화 상태일 때만 보이도록 설정 */
}
/* 탭 버튼 활성화 시 스타일 */
.tab-button.active {
  background-color: #ffffff; /* 활성화 배경 */
  color: #000000; /* 활성화 텍스트 색상 */
  font-weight: 600;
}

/* 탭 버튼 포커스 시 스타일 */
.tab-button:focus-visible {
  ring-width: 2px;
  background-color: var(--white);
  ring-offset-width: 2px;
  ring-offset-color: #ffffff;
}

/* 탭 버튼 비활성화 시 스타일 */
.tab-button:disabled {
  pointer-events: none;
  color: #333333;
  opacity: 0.5;
}

.teams-container {
  font-family: Arial, sans-serif;
  background: var(--white);
  width: 100%;
  align-items: center; /* 가로축을 기준으로 중앙 정렬합니다. */

  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}



.team {
  background: var(--background-color);
  border-radius: 8px;
  justify-content: center;

}





.team-details {
  background: #ffffff; /* Light grey background */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow */
  padding: 12px;
  margin: 0 auto 20px auto;
  display: flex;
  width: 95%;
  flex-direction: column;
  transition: box-shadow 0.3s ease;
  align-items: flex-start; /* Align items to the start of the flex container */
}

.team-details:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.team-info-container {
  display: flex;
  width: 100%;
  align-items: center;
  background: #f8f9fa; /* 연한 배경색으로 구분감을 줍니다 */
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 가독성을 위한 약간의 그림자를 추가합니다 */
}


.team-image {
  width: 40px;
  height: 40px;
  border-radius: 25%;
  margin-right: 15px;
}

.team-info {
  line-height: 1.2; /* 1.2는 글꼴 크기의 120%를 의미합니다 */
}

.team-name {
  text-align: start;
  font-size: 19px;
  font-weight: 600;
  color: #333;
}


.team-description {
  text-align: start;
  font-size: 13px;
  font-weight: lighter;
  color: #838383;
}


.team-member-container {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.team-member {
  display: flex;
  margin: 20px 10px 4px 0;
  flex-direction: column;
  background-image: linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%);
  padding: 10px 15px 10px 15px;
  width: 90px;
  height: 90px;
  text-align: center;
  line-height: 1.1; /* 1.2는 글꼴 크기의 120%를 의미합니다 */
  border-radius: 6px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 이미지에 그림자 추가 */
}

.team-member-photo {
  width: 35px; /* 이미지 크기 조절 */
  height: 35px; /* 이미지 크기 조절 */
  object-fit: cover; /* 이미지 비율을 유지하면서 요소에 맞게 조정 */
  border-radius: 50%;
  margin-bottom: 12px;
  margin-left: auto;
  margin-right: auto;
}

.team-member .user-nickname {
  font-size: 12px;
  font-weight: 600;
  color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "맑은 고딕", helvetica, "Apple SD Gothic Neo", sans-serif;
}

.team-member .user-role {
  font-size: 9px;
  color: #838383;
}



.add-button {
  background-color: #4C516D; /* Semi-transparent blue color */
  color: white;
  border: 1px solid rgba(50, 58, 65, 0.9); /* Solid blue border for contrast */
  border-radius: 6px; /* Makes the button round */
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  margin-top: 10px;
  justify-content: center;
  align-items: center;
  width: 200px;
  position: relative; /* Needed to position the pseudo-element */
}

.add-button::before {
  content: "+"; /* Adds the plus sign */
  font-size: 24px; /* Size of the plus sign */

}

.add-button:hover {
  background-color: var(--accent-color); /* Darker transparent blue on hover */
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






.select-container {
  text-align: center;
  margin-top: 10px;
}

.select-container select {
  padding: 10px;
  margin-right: 5px;
}



.team-member:hover {
  transform: translateY(-3px);
  background-color: #ececec;
}


.participants-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px; /* Creates a gap between participant items */
  margin-bottom: 15px;
}



.team-member:hover {
  background-color: #f8f8f8;
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





.dropdown-content .dropdown-item:hover {
  background-color: #f1f1f1;

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





/* User Profile Image Style */


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

  .game-information-container {
    width: 95%;
  }
}


</style>