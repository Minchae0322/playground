<template>
  <div class="team-container">
    <div class="team-header">
      <img :src= team.teamProfileImg class="team-logo">
      <div class="team-info">
        <h1>{{ team.teamName }}</h1>
        <p class="team-type">{{ team.teamSportsEvent }}</p>
      </div>
      <button class="button-teamJoin" @click="clickJoinTeam">加入</button>
    </div>
    <div class="team-history">
      <h2>Team History</h2>
      <p>The team was founded in 1900 and has a rich history of success and resilience. Over the years, they have won
        numerous championships and produced many legendary players.</p>
    </div>
    <div class="team-players">
      <h2>Players</h2>
      <div v-for="user in teamMembers" :key="user.userId" class="team-item">
        <table>
          <thead>
          <tr>
            <th>Player Profile</th>
            <th>Player Name</th>
            <th>Position</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <img :src="user.userProfileImg || defaultImage" class=""/>
            <td>{{ user.userNickname }}</td>
            <td>{{user.userRole}}</td>
          </tr>
          <!-- More players as needed -->
          </tbody>
        </table>
      </div>

    </div>
    <div class="team-achievements">
      <h2>Achievements</h2>
      <ul>
        <li>10x League Champions</li>
        <li>5x Cup Winners</li>
        <!-- More achievements as needed -->
      </ul>
      <button class="learn-more">Learn more</button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";

import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";
const router = useRouter();
const team = ref({
  teamProfileImg: ref(''),
  teamName: '',
  teamSportsEvent: ref(''),
  leaderName: '',
  leaderId: '',

});

const teamMembers = ref([{
  userProfileImg: ref(''),
  userNickname: '',
  userRole: '',
  userId: '',

}])
const apiBaseUrl = "http://localhost:8080";

const props = defineProps({
  teamId: {
    type: Number,
    required:true,
  }
})
onMounted(() => {
  getTeamInfo()
  getTeamMembers()
});


const clickJoinTeam = async () => {
  await validateAccessToken()
  try {
    await axios.post(`${apiBaseUrl}/team/join/${props.teamId}`, {},
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    alert("가입에 성공하였습니다.");
  } catch (error) {
    alert(error.response.data.message);
  }
};
const getTeamMembers = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/team/${props.teamId}/members`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    teamMembers.value = response.data.map(user => ({
      userId: user.userId,
      userNickname: user.userNickname,
      userRole: user.userRole,
      userProfileImg: user.userProfileImg ? `data:image/jpeg;base64,${ user.userProfileImg}` : defaultImage
    }));
  });
};
const getTeamInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/team/${props.teamId}/info`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {

    team.value.teamId = response.data.teamId,
        team.value.teamName = response.data.teamName,
        team.value.teamSportsEvent = response.data.sportsEvent,
        team.value.teamProfileImg = response.data.teamProfileImg ? `data:image/jpeg;base64,${response.data.teamProfileImg}` : defaultImage,
        team.value.leaderName = response.data.leaderName
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

<style scoped>
.team-container {
  width: 70%;
  margin: 20px auto;

}

.button-teamJoin {
  background: var(--accent-color);
  border: 1px solid rgba(50, 58, 65, 0.9); /* Solid blue border for contrast */
  padding: 10px 40px; /* 버튼 패딩 조정 */
  color: white; /* 텍스트 색상 */
  border-radius: 4px;
  margin: 10px 18px 10px auto;
  text-align: left; /* 왼쪽 정렬 */
}

.team-history {
  background: var(--white); /* Light grey background */
  border: 1px solid #ddd; /* Light grey border */
  border-radius: 8px; /* Rounded corners */
  padding: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
  font-family: MiSans-Medium, sans-serif;
}

.team-history h2 {
  font-size: 1.5em;
  color: #333; /* Darker text color for the title */
  margin-bottom: 10px;

}

.team-history p {
  font-size: 1em;
  font-family: MiSans-Light, sans-serif;
  color: #555; /* Slightly lighter text color for the paragraph */
  line-height: 1.6; /* More readable line height */
}

.team-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.team-logo {
  border-radius: 50%;
  width: 100px;
  height: 100px;
  margin-right: 20px;
  background-color: #ddd;
}

.team-info h1 {
  margin: 0;

  font-size: 2em;
}

.team-type {
  color: #666;
  font-size: 1em;
}


.team-achievements {
  font-family: MiSans-Medium, sans-serif;
  border-radius: 8px;
  margin-top: 20px;
  background: var(--white);
  padding: 20px;
}

.team-players {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 20px;
  border-radius: 8px; /* Rounded corners */
  background: var(--white);
}

.team-players h2 {
  font-size: 1.5em;
  color: #333;
  font-family: MiSans-Medium, sans-serif;
  margin-bottom: 10px;
}

.team-players table {
  width: 100%;
  border-collapse: collapse;

  margin-top: 20px; /* Space between title and table */
}


.team-players th {
  font-family: MiSans-Light, sans-serif;
}

.team-players th,
.team-players td {
  text-align: left;
  padding: 12px 8px; /* Adjust the padding to match the design */
  border-bottom: 1px solid #ddd; /* Light gray border for separation */
}

.team-players img {
  margin: 10px 0 10px 20px;
  width: 50px;
  height: 50px;
  background-color: #eee;
  border-radius: 50%;
  display: flex;
  align-items: end;
  justify-content: end;
  box-shadow: 0 3px 6px 0 rgba(29,34,53,.08);
}
.team-players th {
  background: var(--white); /* Light gray background for headers */
  border-bottom: 2px solid #eaeaea; /* Slightly darker border for headers */
}

.team-players tbody tr:hover {
  background-color: #f2f2f2; /* Slightly darker background on hover */
}

.team-players tbody td {

  color: #555; /* Slightly darker text color for content */
}

/* Remove the border from the last row to match the design */
.team-players tr:last-child td {
  border-bottom: none;
}


.team-achievements ul {
  list-style: none;
  padding: 0;
  font-family: MiSans-Light, sans-serif;
}

.team-achievements li {

  background: #f2f2f2;
  margin-bottom: 5px;
  padding: 10px;
  border-radius: 5px;
}

.learn-more {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 20px;
}

.learn-more:hover {
  background-color: #0056b3;
}
</style>