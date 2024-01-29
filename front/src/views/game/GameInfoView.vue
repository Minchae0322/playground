<script setup>
import {onMounted, onUpdated, ref} from "vue";
import axios from "axios";
import {defineEmits} from 'vue';
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";
import {defineProps} from 'vue';
import PlaygroundInfoView from "@/views/playground/PlaygroundInfoView.vue";

const apiBaseUrl = "http://localhost:8080";

const router = useRouter();
const homeTeams = ref({
  matchTeamSide: "HOME",
  subTeams: [{
    teamProfileImg: '',
    users: [{
      userProfileImg: ''
    }]
  }],
  soloTeam: {
    users: [{
      userProfileImg: ''
    }]
  }
})
const awayTeams = ref({
  matchTeamSide: "AWAY",
  subTeams: [{
    teamProfileImg: '',
    users: [{
      userProfileImg: ''
    }]
  }],
  soloTeam: {
    users: [{
      userProfileImg: ''
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

const loggedInUserId = ref('');
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
const buttonText = ref("+")

const emits = defineEmits(['goBack']);

function goBack() {
  emits('goBack'); // 부모 컴포넌트에게 뒤로가기 이벤트 전달
}


onMounted(async () => {
  await getTeamData('HOME', 'Competition')
  await getTeamData('AWAY', 'Competition')
  await clickHomeTeam()
  await getLoggedUserId()
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

const getLoggedUserId = async () => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/user/profile`,
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

// 팀 데이터를 업데이트하는 함수
const getTeamData = async (matchTeamSide, gameType) => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/game/${props.game.gameId}/${gameType}/${matchTeamSide}`, {
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
        homeTeams.value = {
          ...homeTeams.value,
          subTeams: transformedSubTeams,
          soloTeam: {users: transformedSoloTeamUsers}
        };
      } else if (matchTeamSide === 'AWAY') {
        awayTeams.value = {
          ...awayTeams.value,
          subTeams: transformedSubTeams,
          soloTeam: {users: transformedSoloTeamUsers}
        };
      }
    }
  } catch (error) {
    console.error(`Failed to fetch ${matchTeamSide} teams data:`, error);
    // 에러 처리 로직을 추가할 수 있습니다.
  }
};
const sendSoloGameJoinRequest = async () => {
  const isConfirm = confirm("진행하시겠습니까?")
  if (!isConfirm) {
    return
  }
  await validateAccessToken();
  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/soloGameJoin`, {
          matchTeamSide: homeAndAwayTeams.value.matchTeamSide,
        },
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
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
  const isConfirm = confirm("진행하시겠습니까?")
  if (!isConfirm) {
    return
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

const sendTeamJoinRequest = async (subTeamId, teamId) => {
  const isConfirm = confirm("진행하시겠습니까?")
  if (!isConfirm) {
    return
  }
  await validateAccessToken();
  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/teamGameJoin`,
        {
          subTeamId: subTeamId,
          teamId: teamId,
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

const toggleJoinMenu = async function () {
  menuVisible.value = !menuVisible.value;
  buttonText.value = menuVisible.value ? "-" : "+"
};

const clickScroll = async () => {
  await toggleJoinMenu()
};

const toggleUserTeamDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
  getTeamsUserBelongTo()
};

const selectTeam = (team) => {
  selectedTeam.value = team;
  dropdownVisible.value = false;
  const currentScroll = window.pageYOffset || document.documentElement.scrollTop;
};


const cancel = () => {
  isTeamRegistrationModalVisible.value = false;
  // 추가 취소 로직
};

const clickOutOfGame = async (subTeamId) => {
  await validateAccessToken();
  await axios.delete(`${apiBaseUrl}/game/${props.game.gameId}/${subTeamId}/out`,
      {
        headers: {
          'Authorization': getAccessToken()
        }
      }
  )
};

const getTeamsUserBelongTo = function () {
  validateAccessToken();
  axios.get(`${apiBaseUrl}/user/teams`,
      {
        headers: {
          'Authorization': getAccessToken()
        }
      }
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


<template>
  <div  class="game-container">
    <div class="game-info-container">

      <div class="game-details">
        <div class="game-details-name">{{ props.game.gameName }} ( {{props.game.gameType}} )</div>
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
          <img class="host-info" :src="game.hostProfileImg || defaultImage">
          <div class="host-name">{{ game.hostName }}</div>
        </div>
      </div>

      <button class="button-goBack" @click="goBack"></button>


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
        <div>
          <div class="team-details" v-for="(team, index) in homeAndAwayTeams.subTeams" :key="index">
            <router-link :to="{ name:'teamInfo', params: { teamId: team.teamId } }" class="team-info-container">
              <img class="team-image" :src="team.teamProfileImg || defaultImage">
              <div class="team-info">
                <div class="team-name">{{ team.teamName }}</div>
                <div class="team-more"> ></div>
              </div>
            </router-link>
            <div class="line"></div>
            <div class="participants-num">参与人数 {{ team.users.length }}</div>
            <div class="team-member-container">
              <div class="team-member" v-for="(participant, index) in team.users" :key="index">
                <div v-if="participant.userId === loggedInUserId" class="close-marker"
                     @click="clickOutOfGame(team.subTeamId)">X
                </div>
                <img class="team-member-photo" :src="participant.userProfileImg || defaultImage">
                <p class="user-nickname">{{ participant.userNickname }}</p>
                <p class="user-role">{{ participant.userRole }}</p>
              </div>
            </div>
            <button class="add-button" @click="sendTeamJoinRequest(team.subTeamId, team.teamId)"> 参加</button>
          </div>
        </div>

        <div v-if="homeAndAwayTeams.soloTeam">
          <div class="title-soloTeam">个人参加</div>
          <div v-if="homeAndAwayTeams.soloTeam.users && homeAndAwayTeams.soloTeam.users.length > 0">
          <div class="team-details" v-for="(participant, index) in homeAndAwayTeams.soloTeam.users" :key="index">
            <div class="team-member">
              <div v-if="participant.userId === loggedInUserId" class="close-marker" @click="clickOutOfGame">X</div>
              <img class="team-member-photo" :src="participant.userProfileImg || defaultImage">
              <p class="user-nickname">{{ participant.userNickname }}</p>
              <p class="user-role">个人</p>
            </div>
          </div>
          </div>
          <div class="no-participants" v-else>
            <p >개인팀원이 없습니다.</p>
          </div>
        </div>


        <div id="app" class="flex justify-center items-center h-screen">
          <div class="relative">
            <div v-if="menuVisible" ref="menu" class="py-1" role="menu" aria-orientation="vertical"
                 aria-labelledby="options-menu">
              <div @click="sendSoloGameJoinRequest" class="menu-item" role="menuitem">个人</div>
              <div @click="clickTeamRegistration" class="menu-item" role="menuitem">팀으로 참가하기</div>
            </div>
            <button
                class="select-team-solo-button"
                @click="clickScroll"
                aria-haspopup="menu"
                :aria-expanded="menuVisible.toString()"
            >
              {{ buttonText }}
            </button>

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
        <button @click="cancel"
                class="button-cancel inline-flex items-center justify-center rounded-md text-sm font-medium  px-4 py-2">
          Cancel
        </button>
        <button @click="sendTeamRegistrationRequest"
                class="button-confirm inline-flex items-center justify-center rounded-md text-sm font-medium  px-4 py-2">
          Confirm
        </button>
      </div>
    </div>
  </div>
</template>


<style scoped>


body {
  background: var(--white);
  color: #333333;

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

}

button:hover {
  opacity: 0.8;
}

a {

  font-size: 17px;
  text-decoration: none;
}

/* 게임 정보 컨테이너 및 기본 스타일 */
.game-container {
  text-align: center;
  align-items: center;
  justify-content: center;
  display: flex;
  flex-direction: column;
  background: var(--white);
}

.game-info-container {
  width: 100%;
  background: var(--white);
}




.button-goBack {

  padding: 10px 20px;
  color: black;
  border-radius: 4px;
  margin: 10px auto 10px 25px;
  text-align: left;
  position: relative; /* 상대 위치 설정 */
}

.button-goBack::before {
  content: '←'; /* 화살표 문자 */
  position: absolute; /* 절대 위치 설정 */
  left: 10px; /* 왼쪽 패딩과 일치하도록 설정 */
  top: 50%; /* 버튼 중앙에 위치하도록 설정 */
  transform: translateY(-50%); /* Y축 기준으로 50%만큼 이동하여 중앙 정렬 */
  font-size: 1.2em; /* 화살표 크기 조정 */

}

.button-goBack span {
  visibility: hidden; /* 버튼 내 기존 텍스트 숨김 */
}

.tab-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: var(--white);
  width: 100%;
  margin: 10px 0;
  height: auto;
}

/* 탭 리스트 스타일 */
.tab-list {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: var(--background-color-gray);
  border-radius: 8px;
  width: 95%;
}

/* 탭 버튼 기본 스타일 */
.tab-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  font-size: 13px;
  font-family: MiSans-Light, sans-serif;
  cursor: pointer;
  width: 40%;
  margin: 5px 0;
  border: none;
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
  font-family: MiSans-Heavy, sans-serif;
  background-color: #ffffff; /* 활성화 배경 */
  color: #3f3f3f; /* 활성화 텍스트 색상 */
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

.teams-container {
  background: var(--background-color-gray);
  width: 100%;
  align-items: center; /* 가로축을 기준으로 중앙 정렬합니다. */
  margin: 0 0 0;
  padding: 5px 0;

  transition: box-shadow 0.3s ease;
}


.team {
  background: var(--background-color-gray);
  border-radius: 8px;
  justify-content: center;

}


.line {
  width: 100%;
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
.no-participants {
  padding: 40px;
  margin: 10px 0;
  background-color: white;
  border-radius: 8px;

  text-align: center;
}

.no-participants p{
  text-align: center;
  margin: auto;
  background-color: white;
  border-radius: 8px;
}


.team-details {
  background: #ffffff; /* Light grey background */
  border-radius: 8px; /* Rounded corners */
  padding: 7px;
  margin: 10px auto 10px auto;
  display: flex;
  width: 100%;
  flex-direction: column;
  transition: box-shadow 0.3s ease;
  align-items: flex-start; /* Align items to the start of the flex container */
}

.team-details:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  flex-direction: column;
  transition: box-shadow 0.3s ease;
}

.not-exist-container {
  background-color: white;
  width: 100%;
  height: 50px;
}

.team-info-container {
  display: flex;
  align-items: center;
  padding: 5px 10px 5px 10px;
  border-radius: 5px;
}


.team-image {
  width: 40px;
  height: 40px;
  border-radius: 15%;
  margin-right: 15px;
}

.team-info {
  display: flex;
  flex-direction: row;
  text-align: center;
  justify-content: center;
  align-items: center;
  line-height: 1.2; /* 1.2는 글꼴 크기의 120%를 의미합니다 */
}

.team-name {
  text-align: start;
  font-size: 18px;
  letter-spacing: 4px;

  font-weight: 600;
  color: #333;
}

.team-more {
  margin-left: 5px;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 15px;
  border: 2px solid #838383;
  color: #838383;
}

.team-description {
  text-align: start;
  font-size: 13px;
  font-weight: lighter;
  color: #838383;
}


.team-member-container {
  display: flex;
  width: 100%;
  flex-direction: row;
  align-items: center;
  position: relative; /* 상대 위치 설정 */
}

.close-marker {
  position: absolute; /* 절대 위치 설정 */
  top: 0; /* 상단에 붙임 */
  right: 0; /* 우측에 붙임 */
  cursor: pointer; /* 마우스 포인터 스타일 변경 */
  /* 필요한 추가 스타일 */
}

.team-member {
  display: flex;
  flex-direction: column;
  background: white;
  padding: 10px 5px 5px 5px;
  width: 10%;
  min-width: 70px;
  height: 90px;
  text-align: center;
  line-height: 1.1;
  border-radius: 6px;
}

.team-member-photo {
  width: 35px; /* 이미지 크기 조절 */
  height: 35px; /* 이미지 크기 조절 */
  object-fit: cover; /* 이미지 비율을 유지하면서 요소에 맞게 조정 */
  border-radius: 50%;
  margin: 4px auto 3px;
}

.team-member .user-nickname {
  font-size: 12px;
  font-weight: 600;
  color: black;
  display: flex;
  align-items: center;
  justify-content: center;

}

.team-member .user-role {
  font-size: 9px;
  color: #838383;
}

.title-soloTeam {
  text-align: start;
  margin-left: 5px;
  color: black;
}


.add-button {
  background-color: #4C516D; /* Semi-transparent blue color */
  color: white;
  border: 1px solid rgba(50, 58, 65, 0.9); /* Solid blue border for contrast */
  border-radius: 4px; /* Makes the button round */
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  margin-top: 10px;
  letter-spacing: 4px;
  justify-content: center;
  align-items: center;
  width: 200px;
  font-weight: 600;

  position: relative; /* Needed to position the pseudo-element */
}

.add-button::before {
  content: "+"; /* Adds the plus sign */
  font-size: 24px; /* Size of the plus sign */

}

.add-button:hover {
  background-color: var(--accent-color); /* Darker transparent blue on hover */
}


.select-team-solo-button {
  padding: 8px 16px;
  border: 1px solid #252525;
  width: 95%;
  border-radius: 4px;
  margin-bottom: 5px; /* 필요에 따라 조정 */
  margin-top: 10px;
  font-size: 14px;
  cursor: pointer;
  outline: none;
  background: var(--accent-color);
  z-index: 1000; /* 메뉴가 버튼 아래에 오도록 z-index 설정 */
}

.select-team-solo-button:hover {
  background-color: var(--secondary-color)
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
  font-family: MiSans-Semibold, sans-serif;
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



.select-container select {
  padding: 10px;
  margin-right: 5px;
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
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
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
  background: var(--accent-color);
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
  background-color: var(--secondary-color); /* 마우스 오버 시 배경색 변경 */
  color: #ffffff; /* 마우스 오버 시 글자색 변경 */
}


@media (max-width: 768px) {

}


</style>