<template>
  <body>
  <div class="userInfo-container">
    <h1>我的信息</h1>
    <div class="header">
      <div class="userProfile-container">
        <img :src="user.userProfileImg || defaultImage" @click="triggerFileInput" class="profile-image" />
        <input type="file" ref="fileInput" @change="handleFileChange" style="display:none" />
      <div class="profile-hint">点击照片更改头像</div>
      </div>
      <div class="nickname-container" v-if="!isEditing">
        <div class="user-nickname">{{ user.userNickname }}</div>
        <button class="nickname-change-button" @click="clickChangeNickname">换昵称</button>
      </div>
      <div class="nickname-container" v-else>
        <input v-model="editedNickname" placeholder="Enter new nickname" />
        <div class="button-container">
          <button @click="clickChangeNickname">取消</button>
          <button @click="confirmEditNickname">确定</button>
        </div>
      </div>
    </div>

    <div class="teams">
      <div class="teams-title">
        <h3>Teams</h3>
      </div>
      <div v-for="team in teams" :key="team.teamId" class="team-item">
        <router-link class="team-container" :to="{ name:'teamInfo', params: { teamId: team.teamId } }">
          <img :src="team.teamProfileImg || defaultImage" class="team-image"/>
          <div class="teams-info">
            <h2 class=" team-name">{{ team.teamName }}</h2>
            <h2 class="team-sportsEvent">종목: {{ team.teamSportsEvent }}</h2>
          </div>
        </router-link>
      </div>
      </div>
    </div>
  </body>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import defaultImage from '../assets/img.png';
import {useRouter} from "vue-router";


const apiBaseUrl = "http://13.125.38.164/api";
const router = useRouter();
const editedNickname = ref("")
const isEditing = ref(false); // 닉네임 편집 상태를 추적하는 반응형 변수
const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})

const teams = ref([{
  teamId: '1',
  teamName: '',
  teamProfileImg: ref(''),
  teamSportsEvent: '',

}]);

onMounted(async () => {
  // Check if the initial page number is provided in the route query
  await getUserInfo()
  getTeams()
});
const getUserInfo = async () => {
  await validateAccessToken()
  axios.get(`${apiBaseUrl}/user/info`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    user.value.userNickname = response.data.userNickname
    user.value.userProfileImg = `data:image/jpeg;base64,${response.data.userProfileImg}`;
  });

};

const confirmEditNickname = function () {
  validateAccessToken()
  axios.patch(`${apiBaseUrl}/user/change-nickname`,{
    userNickname: editedNickname.value
      },
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    user.value.userNickname = response.data.userNickname
    alert("닉네임이 변경되었습니다.")
    clickChangeNickname()
  }).catch(error => {
    // 에러 메시지 처리
    const errorMessage = extractErrorMessage(error);
    alert(errorMessage);
  });
  function extractErrorMessage(error) {
    if (error.response && error.response.data) {
      if (error.response.data.errors) {
        // 여러 에러 메시지가 있는 경우 (예: 유효성 검사 실패)
        return error.response.data.errors.map(e => e.message).join('\n');
      } else if (error.response.data.message) {
        // 단일 에러 메시지가 있는 경우
        return error.response.data.message;
      }
    }
    return 'error'; // 기본 에러 메시지
  }
};
const getTeams =  () => {
  validateAccessToken();
  axios.get(`${apiBaseUrl}/user/teams`, {
    headers: {
      'Authorization': localStorage.getItem("accessToken")
    }
  }).then(response => {
    teams.value = response.data.map(team => ({
      teamId: team.teamId,
      teamName: team.teamName,
      teamSportsEvent: team.sportsEvent,
      teamProfileImg: team.teamProfileImg ? `data:image/jpeg;base64,${team.teamProfileImg}` : defaultImage
    }));
    console.log(teams.value[0].teamId)
  }).catch(error => {
    console.error('팀 정보를 가져오는데 실패했습니다.', error);
  });
};

const fileInput = ref(null);

const triggerFileInput = () => {
  fileInput.value.click();
};

const clickChangeNickname = function () {
  isEditing.value = !isEditing.value;
};
const handleFileChange = async (event) => {
  await validateAccessToken()
  const file = event.target.files[0];
  if (!file) {
    console.log('파일을 선택해주세요.');
    return;
  }

  const formData = new FormData();
  formData.append('image', file); // 'image'는 백엔드에서 기대하는 필드명입니다.

  try {
    const response = await axios.post(`${apiBaseUrl}/user/profile-img/update`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem("accessToken")
      },
    });
    router.go(0)
    console.log('업로드 성공', response.data);
  } catch (error) {
    console.error('업로드 실패', error);
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
body {
  margin: 0;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  background: var(--background-color-gray); /* 배경색을 추가할 수도 있습니다 */
}

.userInfo-container {
  max-width: 50%;
  margin: auto;
  width: 50%; /* 너비를 50%로 설정 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 5px 1px 8px 0 rgba(0,0,0,.06);
  border-left: 1px solid rgba(0,0,0,.08);;

  background: #fff; /* 카드의 배경색 */
  overflow: auto; /* 내용이 넘칠 때 스크롤바를 보여줌 */
}

.userProfile-container img {
  width: 100px;
  height: 100px;
  background-color: #eee;
  border-radius: 50%;
  display: inline-block;
  box-shadow: 0 3px 6px 0 rgba(29,34,53,.08);
}

.profile-hint {
  color: var(--text-hint);
  font-family: MiSans-Light,sans-serif;
  font-size: 11px;
}
.header {
  text-align: center;
  margin-bottom: 30px;

}

.header h4 {
  font-size: 10px;
  color: #838383;
  margin-bottom: 10px;
}

.nickname-container {
  display: flex; /* flexbox 레이아웃 사용 */
  justify-content: center; /* 가운데 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  flex-direction: column; /* 아이템을 수직 방향으로 정렬 */
}

.user-nickname {
  font-size: 23px;
  color: black;
}

.nickname-change-button{
  font-family: MiSans-Medium, sans-serif;
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  background-color: #f5f5f5;
  cursor: pointer;
}


.nickname-container input {
  width: 40%; /* 크기를 40%로 설정 */
  padding: 10px; /* 패딩 추가 */
  border: 1px solid #ccc; /* 테두리 스타일 */
  border-radius: 5px; /* 둥근 테두리 */
  margin-bottom: 10px; /* 요소 간의 여백 추가 */
  font-size: 14px; /* 폰트 크기 */
  background-color: #f5f5f5; /* 배경색 */
}

.nickname-container input:focus {
  outline: none; /* 포커스 시 외곽선 제거 */
  border-color: #4CAF50; /* 포커스 시 테두리 색상 변경 */
  box-shadow: 0 0 5px rgba(76, 175, 80, 0.5); /* 포커스 시 그림자 효과 */
}
.nickname-container button {
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--accent-color);
  width: 20%;
  color: white;
  padding: 10px;
  margin-top: 10px;
  font-family: MiSans-Medium,sans-serif;
  overflow-y: hidden;
  overflow-x: hidden;
  border: none;
  border-radius: 5px;
}
.nickname-container .button-container {
  display: flex;
  justify-content: center;
  width: 40%;
}

.nickname-container .button-container button {
  flex: 1; /* 버튼의 크기를 같게 조정 */
  margin: 5px; /* 버튼 사이의 간격 */
}
.nickname-container input {
  width: 40%; /* 크기를 40%로 설정 */

  margin-bottom: 10px; /* 요소 간의 여백 추가 */
}


.teams {
  height: 100%;
  background: #f9fbfc; /* 배경색을 추가할 수도 있습니다 */
}
.router-link h2 {
  font-family: gothic-bold;
  text-decoration: none;
}

.team-container {
  display: flex;
  justify-content: start;
  align-items: center;
}

.team-container img {
  width: 50px; /* 적절한 크기 설정 */
  height: 50px; /* 적절한 크기 설정 */
  border-radius: 25%;
  margin-right: 20px;
}

.teams-info {
  display: flex;
  flex-direction: column;
  text-align: start;
}


.teams h3 {
  display: flex;
  margin-left: 10px;
  margin-top: 10px;
  text-align: start;
  align-items: center;
}

.teams ul {
  list-style: none;
  padding: 0;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  flex-wrap: wrap;
}

.team-item {
  background-color: #ffffff;
  padding: 10px;
  margin: 5px;
  border-radius: 5px;
  text-align: center;

  flex-basis: calc(33.333% - 10px); /* 3개의 아이템을 한 줄에 나타내고 싶을 때 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}



.team-name {
  font-size: 14px; /* 적절한 크기 설정 */
  color: #333;

}

.teams-title {
  display: flex;
  align-items: center;
  border-radius: 12px 12px 0 0;
  background: var(--primary-gradient);

  color: white;
  padding: 0 5px 10px;
}
.team-sportsEvent {
  font-size: 14px; /* 적절한 크기 설정 */
}
/* 추가적으로 반응형 디자인을 고려할 수 있습니다. */
a {
  text-decoration: none;

}
</style>