<template>
  <body>


  <div class="userInfo-container">
    <div class="header">
      <img :src="user.userProfileImg || defaultImage" @click="triggerFileInput" class="profile-image" />
      <input type="file" ref="fileInput" @change="handleFileChange" style="display:none" />
      <p>사진을 클릭하여 프로필을 변경하세요.</p>

      <div class="nickName-edit-container" v-if="isEditing">
        <h2>{{ user.userNickname }}</h2>
        <button @click="clickChangeNickname">닉네임 변경</button>
      </div>
      <div v-else>
        <input v-model="editableNickname" placeholder="Enter new nickname" />

        <button @click="editNickname">Edit Nickname</button>
      </div>
    </div>
    <p>This is a short bio about the user. It provides some general information about the user's interests, hobbies, or profession.</p>
    <button>Edit Profile</button>
    <button>View Public Profile</button>
    <div class="teams">
      <h3>Teams</h3>
      <li v-for="(team, index) in teams" :key="index" class="team-item">
        <img :src="team.teamProfileImg || defaultImage" class="team-image" />
        <p class="team-name">{{ team.teamName }}</p>
      </li>
    </div>
  </div>
  </body>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import defaultImage from '../assets/img.png';
import {useRouter} from "vue-router";

const apiBaseUrl = "http://localhost:8080";
const router = useRouter();

const isEditing = ref(true); // 닉네임 편집 상태를 추적하는 반응형 변수
const userProfileImg = ref('');
const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})

const teams = ref([{
  teamName: '',
  teamProfileImg: ref('')
}]);

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getUserInfo()
  getTeams()
});
const getUserInfo = function () {
  validateAccessToken()
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
const getTeams = async () => {
  validateAccessToken();
  try {
    const response = await axios.get(`${apiBaseUrl}/user/teams`, {
      headers: {
        'Authorization': localStorage.getItem("accessToken")
      }
    });
    teams.value = response.data.map(team => ({
      teamName: team.teamName,
      teamProfileImg: team.teamProfileImg ? `data:image/jpeg;base64,${team.teamProfileImg}` : ''
    }));
  } catch (error) {
    console.error('팀 정보를 가져오는데 실패했습니다.', error);
  }
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
  background: #f5f5f5; /* 배경색을 추가할 수도 있습니다 */
}

.nickName-edit-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* 상단 여백 추가 */
}

.nickName-edit-container h2 {
  margin: 10px 0; /* 제목 주변 여백 */
}

.nickName-edit-container button {
  padding: 8px 15px; /* 버튼 패딩 */
  background-color: #4CAF50; /* 버튼 배경색 */
  color: white; /* 버튼 텍스트 색상 */
  font-size: 14px; /* 텍스트 크기 */
  border: none; /* 테두리 제거 */
  width: 20%;
  border-radius: 4px; /* 테두리 둥글게 */
  cursor: pointer; /* 마우스 오버시 커서 변경 */
  transition: background-color 0.3s; /* 배경색 변화 애니메이션 */
}

.nickName-edit-container button:hover {
  background-color: #45a049; /* 호버 시 버튼 색상 변경 */
}

/* 입력 필드 스타일링 */
.nickName-edit-container input {
  padding: 10px;
  margin-bottom: 10px; /* 입력 필드와 버튼 사이 여백 */
  border: 1px solid #ccc; /* 테두리 색상 */
  border-radius: 4px; /* 테두리 둥글게 */
  font-size: 14px; /* 텍스트 크기 */
}

.userInfo-container {
  max-width: 50%;
  margin: auto;
  width: 50%; /* 너비를 50%로 설정 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  font-family: 'Arial', sans-serif;
  background: #fff; /* 카드의 배경색 */
  overflow: auto; /* 내용이 넘칠 때 스크롤바를 보여줌 */
}

.header {
  text-align: center;
  margin-bottom: 20px;

}

.header p {
  font-size: 10px;
  color: #838383;
}

.profile-image {
  width: 80px;
  height: 80px;
  background-color: #eee;
  border-radius: 50%;
  display: inline-block;
  margin-bottom: 10px;
}

h2 {
  margin: 0;
}

.verified {
  background: green;
  color: white;
  padding: 2px 6px;
  border-radius: 5px;
  display: inline-block;
  margin-top: 10px;
}

button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  background-color: #f5f5f5;
  cursor: pointer;
}

button:hover {
  background-color: #e1e1e1;
}

.teams h3 {
  margin-top: 20px;
  text-align: center;
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
  background-color: #f5f5f5;
  padding: 10px;
  margin: 5px;
  border-radius: 5px;
  text-align: center;
  flex-basis: calc(33.333% - 10px); /* 3개의 아이템을 한 줄에 나타내고 싶을 때 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.team-image {
  width: 50px; /* 적절한 크기 설정 */
  height: 50px; /* 적절한 크기 설정 */
  border-radius: 50%;
  margin-bottom: 10px;
}

.team-name {
  font-size: 14px; /* 적절한 크기 설정 */
  color: #333;
  margin-top: 5px;
}

/* 추가적으로 반응형 디자인을 고려할 수 있습니다. */

</style>