<template>
  <div class="team-building-form-container">
    <h1>Team Building Form</h1>
    <p>Create your dream team. Complete the form below and hit submit when you're ready.</p>

    <form @submit.prevent="submitForm">
      <div v-if="team.imagePreview" class="image-preview">
        <img :src="team.imagePreview" alt="+">
      </div>
      <h3 class="team-name">{{team.name}}</h3>
      <div class="file-upload-wrapper">
        <input type="file" id="profilePicture" @change="onFileChange" class="file-input" hidden>
        <button type="button" onclick="document.getElementById('profilePicture').click()" class="file-upload-button">
          Choose File
        </button>

      </div>
      <div class="form-group">
        <label for="teamName">Team Name</label>
        <input type="text" id="teamName" v-model="team.name" placeholder="Enter team name">
      </div>
      <div class="sports-category-container">
        <h4 class="">Sports Type</h4>
        <img src="../assets/soccer-ball.png" alt="" :class="{ 'selected': sportsEvent === 'Soccer' }"
             @click="selectCategory('Soccer')">
        <img src="../assets/baseball-ball.png" alt="" :class="{ 'selected': sportsEvent === 'Baseball' }"
             @click="selectCategory('Baseball')">
      </div>


      <div class="form-group">
        <label for="teamLeader">Team Leader</label>
        <div class="leader-container">
        <img class="leaderImg" :src=leader.userProfileImg >
        <h3  id="teamLeader" >{{leader.userNickname}}</h3>
        </div>
      </div>
      <div class="form-group">
        <label for="teamGoals">Team Description</label>
        <textarea id="teamGoals" v-model="team.description" placeholder="Enter team goals"></textarea>
      </div>
      <button type="submit" @click="write">Submit</button>
    </form>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import defaultImage from '../assets/img.png';
import axios from "axios";

const leader = ref({
  userNickname: '',
  userProfileImg: '',
})
const apiBaseUrl = "http://localhost:8080";
const sportsEvent = ref("Soccer")

onMounted(() => {
  // Check if the initial page number is provided in the route query
 getLeaderProfile()
});

const selectCategory = function (category) {
  sportsEvent.value = category; // 'Soccer' 또는 'Baseball'로 설정됩니다.
  console.log(sportsEvent.value)
};

const team = ref({
  name: '',
  description: '',
  profilePicture: 'null',
  imagePreview: defaultImage,
});

const getLeaderProfile = function () {
  validateAccessToken()
  const accessToken = getAccessToken();
  if (accessToken) {
    axios.get(`${apiBaseUrl}/user/info`, {
      headers: {
        'Authorization': accessToken
      }
    }).then(response => {
      leader.value.userNickname = response.data.userNickname
      leader.value.userProfileImg =  `data:image/jpeg;base64,${response.data.userProfileImg}`;
    });
  }
};

const onFileChange = (e) => {
  const file = e.target.files[0];
  team.value.profilePicture = file;

  const reader = new FileReader();
  reader.onload = (e) => {
    team.value.imagePreview = e.target.result;
  };
  reader.readAsDataURL(file);
};


const frm = new FormData();
const write = function () {
  // FormData 인스턴스 생성
  const formData = new FormData();

  // 팀 정보 추가
  formData.append("team", new Blob([JSON.stringify({
    teamName: team.value.name,
    teamDescription: team.value.description,
    sportsEvent: sportsEvent.value
  })], {
    type: "application/json"
  }));

  // 파일이 존재하는 경우에만 파일을 FormData에 추가
  if (team.value.profilePicture) {
    formData.append("imageFile", team.value.profilePicture);
  }

  // Axios로 POST 요청 보내기
  axios.post(`${apiBaseUrl}/team/build`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': localStorage.getItem("accessToken")
    },
  })
      .then(response => {
        // 성공 시 처리 로직
        console.log("글 작성 완료", response);
        // TODO: 성공적으로 글을 작성한 후 필요한 로직을 여기에 추가하세요.
        // 예: this.$router.push('/posts');
      })
      .catch(error => {
        // 에러 메시지 처리
        const errorMessage = extractErrorMessage(error);
        alert(errorMessage);
      });
};

// 에러 메시지 추출 함수
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
  return '글 작성 중 문제가 발생했습니다.'; // 기본 에러 메시지
}

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
.team-building-form-container {
  max-width: 35%;
  margin:  auto;
  padding: 20px 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.leaderImg {

  width: 40px;
  height: 40px;
  margin: 10px 20px;

  border-radius: 25%;
  border: 2px solid #c2c2c2;
  cursor: pointer;

}

.team-name {
  text-align: center;
  margin-bottom: 20px;
  font-weight: bold;
  letter-spacing: 5px;
  font-family: primary-font,sans-serif;
}

.team-building-form-container h1 {
  text-align: start;
  font-weight: bold;
}

.team-building-form-container h4 {
  font-weight: bold;
  font-family: primary-font,sans-serif;
}

.leader-container {
  margin-left: 10px;
  display: flex;
  margin-bottom: 10px;
}

.file-upload-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.file-input {
  width: 1px;
  height: 1px;
  opacity: 0;
  overflow: hidden;
  position: absolute;
  z-index: -1;
}

.file-upload-button {
  background-color: #000000; /* Green */
  color: white;

  border: none;
  width: 25%;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  margin-bottom: 10px;
}

.file-upload-button:hover {
  background-color: rgba(35, 83, 134, 0.74);
}

.sports-category-container {
  display: flow;

}
.selected {
  border: 3px solid #e0c200; /* 또는 원하는 색상 코드 */
}

/* 이미지 기본 스타일 */

.sports-category-container img {
  width: 40px;
  height: 40px;
  margin: 10px;
  border-radius: 50%;
  cursor: pointer;
  transition: border-color 0.3s; /* 부드러운 테두리 색상 전환 효과 */
}
.team-building-form-container p {
  text-align: start;
  color: #838383;
  font-size: 12px;
  margin-bottom: 10px;
}

.form-group {
  margin-bottom: 5px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 10px;
}

.form-group input[type="text"],
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #e3e3e3;
  border-radius: 4px;
  margin-bottom: 10px;
}
.image-preview {
  display: flex;
  justify-content: center; /* Centers the child horizontally */
  align-items: center; /* Centers the child vertically */
  margin-bottom: 15px; /* Optional: adds some space below the image preview */
}

.image-preview img {
  display: flex;
  width: 80px;
  border: 1px solid #ccc;
  border-radius: 16px;
  height: 80px;
  justify-content: center; /* Centers the child horizontally */
  align-items: center; /* Centers the child vertically */
  object-fit: cover;
}
.form-group input[type="text"] {
  height: 35px;
}



.form-group textarea {
  height: 80px;
  resize: vertical;
}

button {
  width: 100%;
  padding: 10px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>