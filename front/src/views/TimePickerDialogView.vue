<template>
  <div class="team-container">
    <div class="team-header">
      <img src="../assets/img.png" alt="Team Logo" class="team-logo">
      <div class="team-info">
        <h1>The Team Name</h1>
        <p class="team-type">Professional Sports Team</p>
      </div>
    </div>
    <div class="team-history">
      <h2>Team History</h2>
      <p>The team was founded in 1900 and has a rich history of success and resilience. Over the years, they have won
        numerous championships and produced many legendary players.</p>
    </div>
    <div class="team-players">
      <h2>Players</h2>
      <table>
        <thead>
        <tr>
          <th>Player Name</th>
          <th>Position</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>John Doe</td>
          <td>Forward</td>
        </tr>
        <tr>
          <td>Jane Smith</td>
          <td>Goalkeeper</td>
        </tr>
        <!-- More players as needed -->
        </tbody>
      </table>
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
const router = useRouter();
const team = ref([])
const apiBaseUrl = "http://localhost:8080";

const getTeamInfo = function () {
  validateAccessToken()
  const accessToken = getAccessToken()
  if(accessToken) {
    axios.get(`${apiBaseUrl}/game/34/homeTeams`,
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
  font-family: Arial, sans-serif;
}

.team-history {
  background: #f9f9f9; /* Light grey background */
  border: 1px solid #ddd; /* Light grey border */
  border-radius: 4px; /* Rounded corners */
  padding: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.team-history h2 {
  font-size: 1.5em;
  color: #333; /* Darker text color for the title */
  margin-bottom: 10px;
}

.team-history p {
  font-size: 1em;
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
  border-top: 2px solid #000;
  padding-top: 20px;
  margin-top: 20px;
}

.team-players {
  margin-top: 20px;
  margin-bottom: 20px;
}

.team-players h2 {
  font-size: 1.5em;
  color: #333;
  margin-bottom: 10px;
}

.team-players table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px; /* Space between title and table */
}

.team-players th,
.team-players td {
  text-align: left;
  padding: 12px 8px; /* Adjust the padding to match the design */
  border-bottom: 1px solid #ddd; /* Light gray border for separation */
}

.team-players th {
  font-weight: bold;
  background-color: #f8f8f8; /* Light gray background for headers */
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