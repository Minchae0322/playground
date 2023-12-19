<template>
  <div class="team-building-form-container">
    <h1>Team Building Form</h1>
    <p>Create your dream team. Complete the form below and hit submit when you're ready.</p>

    <form @submit.prevent="submitForm">
      <div v-if="team.imagePreview" class="image-preview">
        <img :src="team.imagePreview" alt="+">
      </div>
      <div class= "profile-container">
        <input type="file" id="profilePicture" @change="onFileChange">
      </div>


        <div class="form-group">
        <label for="teamName">Team Name</label>
        <input type="text" id="teamName" v-model="team.name" placeholder="Enter team name">
      </div>
      <div class="form-group">
        <label for="teamLeader">Team Leader</label>
        <h3 type="text" id="teamLeader" >{{leader.userNickname}}</h3>
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

const leader = ref("")
const apiBaseUrl = "http://localhost:8080";

onMounted(() => {
  // Check if the initial page number is provided in the route query
 getLeaderProfile()
});


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
    axios.get(`${apiBaseUrl}/user/profile`, {
      headers: {
        'Authorization': accessToken
      }
    }).then(response => {
      leader.value = response.data
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

const submitForm = () => {
  const formData = new FormData();
  formData.append('name', team.value.name);
  formData.append('leader', team.value.leader);
  formData.append('members', team.value.members);
  if (team.value.profilePicture) {
    formData.append('profilePicture', team.value.profilePicture);
  }

  console.log('Form submitted!', formData);
  // Send formData to your server using axios or another method
};

const frm = new FormData();
const write = function () {
  // const file = fileList.value[0];
  if (team.value.name.trim() === "") {
    alert("팀 이름은 공백일 수 없습니다.")
    return; // Stop here and don't proceed with the API call
  }
  // 새로운 코드: fileList.value를 사용하여 파일 가져오기

  const file = team.value.profilePicture

  frm.append("team", new Blob([JSON.stringify({ teamName: team.value.name, teamDescription: team.value.description})], { type: "application/json" }));
  frm.append("imageFile", file);


  axios
      .post(`${apiBaseUrl}/team/build`, frm, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem("accessToken")
        },
      })
      .then((response) => {
        // Handle the successful write action here
        console.log("글 작성 완료", response);

        // Redirect to /posts route
      })
      .catch((error) => {
        console.error('글 작성 오류:', error);});
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
  max-width: 30%;
  margin:  auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.team-building-form-container h1 {
  text-align: center;
}

.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.team-building-form-container p {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input[type="text"],
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
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
  width: 100px;
  border: 1px solid #ccc;
  border-radius: 16px;
  height: 100px;
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
  background-color: rgba(35, 83, 134, 0.74);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>