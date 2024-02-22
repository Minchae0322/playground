<!--<template>
  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-soccerball.png">
      <h2>足球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>

  <div v-if="soccerGames.length === 0 || !soccerGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>


  <div v-else class="upcoming-games-container-home">
    <div class="games-home">
      <div class="game-card-mini" v-for="game in soccerGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div class="game-card-mini-info">主持人: {{ game.hostName }}</div>
            <div class="game-card-mini-info">所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'SOCCER'}}">
    <button class="more-button-home">更多</button>
  </RouterLink>


  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-basketballBall.png">
      <h2>篮球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="basketballGames.length === 0 || !basketballGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in basketballGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-info-container">
              <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
              <div>主持人: {{ game.hostName }}</div>
              <div>所需时间: {{ game.runningTime }} 分</div>
            </div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}">
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-badmintonBall.png">
      <h2>羽毛球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="badmintonGames.length === 0 || !badmintonGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in badmintonGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>

        </router-link>
      </div>
    </div>
  </div>


  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BADMINTON'}}">
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-tennisBall.png">
      <h2>网球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="tennisGames.length === 0 || !tennisGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in tennisGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
            <div>主持人: {{ game.hostName }}</div>
            <div>所需时间: {{ game.runningTime }} 分</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'TENNIS'}}">
    <button class="more-button-home">更多</button>
  </RouterLink>

  <div class="page-title-container">
    <div class="flex-box">
      <img src="../assets/icon-tableTennisBall.png">
      <h2>乒乓球</h2>
    </div>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="tableTennisGames.length === 0 || !tableTennisGames" class="empty-games-screen">
    <div class="empty-message">
      <h2>没有即将开始的比赛</h2>
      <p>目前没有安排的比赛。</p>
    </div>
  </div>
  <div v-else class="upcoming-games-container">
    <div class="games">
      <div class="game-card" v-for="game in tableTennisGames" :key="game.gameId">
        <router-link
            :to="{ name: 'playground' , params : { playgroundId: game.playgroundId, receivedGameId: game.gameId}}">
          <div class="game-card-mini-title">{{ game.gameName }}
            <div :style="{ color: getDayColor(game.gameStart) }">&nbsp;{{ getChineseDayOfWeek(game.gameStart) }}</div>
          </div>
          <div class="game-card-mini-title">[{{ game.gameStart }}]</div>
          <div class="game-card-mini-info-container">
            <div class="game-card-mini-info-container">
              <div class="game-card-mini-campus-name">地点 : {{ game.playgroundName }} , {{ game.campusName }}</div>
              <div>主持人: {{ game.hostName }}</div>
              <div>所需时间: {{ game.runningTime }} 分</div>
            </div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <RouterLink class="more-button-container-home" :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}">
    <button class="more-button-home">更多</button>
  </RouterLink>


</template>-->