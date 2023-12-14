<template>
  <div class="game-information">
    <h1>Game Information</h1>
    <p>Details about the upcoming match.</p>

    <div class="teams-container">
      <div class="team">
        <h2>Home</h2>
        <div class="team-details" v-for="(team,index) in homeTeams" :key="index">
<!--          <p>{{ team.teamId }}</p>-->
          <p>{{ team.teamName }}</p>
          <div class="participant" v-for="(participant, index) in team.users" :key="index">
            <p>{{ participant.userId }}</p>
            <p>{{ participant.userNickname }}</p>
          </div>
        </div>
        <button class="add-button" @click="addParticipant()"></button>

      </div>

      <div class="team">
        <h2>Away</h2>
        <div class="team-details" v-for="team in awayTeams" :key="team.id">
          <p>{{ team.name }}</p>
          <p>{{ team.value }}</p>
          <div class="participant" v-for="participant in team.participants" :key="participant.id">
            <p>{{ participant.name }}</p>
            <p>{{ participant.position }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Button for more details -->
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";


const homeTeams = ref([])
const awayTeams = ref([])
const apiBaseUrl = "http://localhost:8080/";

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getHomeTeams()
});
const getHomeTeams = function () {
  const accessToken = localStorage.getItem("accessToken");
  if(accessToken) {
    axios.get(`${apiBaseUrl}game/34/homeTeams`,
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

<style scoped>
.game-information {
  width: 90%;
  margin: 20px auto;
  font-family: 'Arial', sans-serif;
  color: #333;
  text-align: center;
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
  justify-content: space-between;
  margin-top: 20px;
}

.team {
  border: 2px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  text-align: left;
  width: calc(50% - 40px); /* Adjust for padding and spacing between teams */
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  background: #fff;
}

.team h2 {
  font-size: 26px;
  margin-bottom: 10px;
  font-weight: bold;
  font-family: primary-font,sans-serif;
  color: #000000;
}

.team-details {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin-bottom: 20px;
  display: flex;
  transition: box-shadow 0.3s ease;
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
.details-button {
  background-color: #4e73df;
  color: white;
  border: none;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  transition: background-color 0.2s;
  outline: none;
  margin-top: 20px;
}

.details-button:hover,
.details-button:focus {
  background-color: #6c8eed;
  outline: none;
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
  padding: 15px;
  margin-bottom: 20px;
  background-color: rgba(70, 130, 180, 0.3); /* Semi-transparent blue color */
  color: white;
  border: 2px solid rgba(70, 130, 180, 0.9); /* Solid blue border for contrast */
  border-radius: 6px; /* Makes the button round */
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 10%;
  position: relative; /* Needed to position the pseudo-element */
}

.add-button::before {
  content: "+"; /* Adds the plus sign */
  font-size: 24px; /* Size of the plus sign */
  line-height: 1; /* Adjust line height to vertically center the plus sign */
}

.add-button:hover {
  background-color: rgba(70, 130, 180, 0.5); /* Darker transparent blue on hover */
}
</style>