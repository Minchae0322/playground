
<template>


</template>

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

const props = defineProps({
  game: {
    type: Object,
    required: true
  }
});
const buttonText = ref("+")

const emits = defineEmits(['goBack']);

onMounted(async () => {
  await getTeamData("", 'Friendly')
});

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

function goBack() {
  emits('goBack'); // 부모 컴포넌트에게 뒤로가기 이벤트 전달
}

const sendFriendlyGameJoinRequest = async () => {
  const isConfirm = confirm("진행하시겠습니까?")
  if (!isConfirm) {
    return
  }
  await validateAccessToken();
  try {
    await axios.post(`${apiBaseUrl}/game/${props.game.gameId}/join/FriendlyGameJoin`, {
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