<template>
  <div class="flex flex-col">
    <div class="flex items-center p-4">
      <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
          @select="handleSelect"
      >

        <div class="flex-grow" />
        <el-sub-menu index="2">
          <template #title>Workspace</template>
          <el-sub-menu index="2-4">
            <template #title>1 캠퍼스</template>
            <el-menu-item index="2-4-1">축구장</el-menu-item>
            <el-menu-item index="2-4-2">농구장</el-menu-item>
            <el-menu-item index="2-4-3">배드민턴장</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
      </el-menu>

    </div>
    <div class="flex flex-col space-y-4 p-4">
      <div class="rounded-lg border bg-card text-card-foreground shadow-sm w-full" data-v0-t="card">
        <div class="flex flex-col space-y-1.5 p-6">
          <h3 class="text-2xl font-semibold leading-none tracking-tight">Playground Item 1</h3>
        </div>
        <div class="p-6">
          <img
              src="../assets/img.png"
              alt="Placeholder"
              class="w-full h-auto"
              width="300"
              height="200"
              style="aspect-ratio:300/200;object-fit:cover"
          />
          <div class="mt-2">
            <p>10:00am - 11:00am</p>
            <p>John Doe</p>
          </div>
        </div>
      </div>

    </div>
  </div>

</template>