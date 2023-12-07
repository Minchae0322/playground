<template>
  <div>
    <div class="p-4 lg:p-6">
      <div class="mx-auto max-w-6xl">
        <div class="shadow-md rounded-lg px-4 py-3 flex items-center justify-between bg-white dark:bg-gray-800">
          <div class="w-full">
            <Input
                class="w-full p-2 rounded border border-gray-300"
                id="search"
                placeholder="Search..."
                type="search"
            />
            <p class="text-sm text-gray-500 dark:text-gray-400 mt-1">Enter your search query.</p>
          </div>
          <Button class="ml-2" size="sm" variant="ghost">
            Search
          </Button>
        </div>
      </div>
    </div>
    <div class="p-4 lg:p-6 max-w-6xl mx-auto">
      <Card class="mb-4">
        <CardContent class="flex gap-4 p-4">
          <Avatar class="h-9 w-9">
            <AvatarImage alt="Item Image" src="/placeholder.svg?height=36&width=36" />
            <AvatarFallback>IM</AvatarFallback>
          </Avatar>
          <div>
            <h2 class="font-bold text-lg">Item Name 1</h2>
            <p class="text-sm text-gray-500 dark:text-gray-400">Item Description 1</p>
          </div>
        </CardContent>
      </Card>
      <Card class="mb-4">
        <CardContent class="flex gap-4 p-4">
          <Avatar class="h-9 w-9">
            <AvatarImage alt="Item Image" src="/placeholder.svg?height=36&width=36" />
            <AvatarFallback>IM</AvatarFallback>
          </Avatar>
          <div>
            <h2 class="font-bold text-lg">Item Name 2</h2>
            <p class="text-sm text-gray-500 dark:text-gray-400">Item Description 2</p>
          </div>
        </CardContent>
      </Card>
      <Card class="mb-4">
        <CardContent class="flex gap-4 p-4">
          <Avatar class="h-9 w-9">
            <AvatarImage alt="Item Image" src="/placeholder.svg?height=36&width=36" />
            <AvatarFallback>IM</AvatarFallback>
          </Avatar>
          <div>
            <h2 class="font-bold text-lg">Item Name 3</h2>
            <p class="text-sm text-gray-500 dark:text-gray-400">Item Description 3</p>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>