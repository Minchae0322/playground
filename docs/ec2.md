## 파일 전송 scp -i C:\Download\playground-key.pem (-r 디렉토리명) or (파일명) ubuntu.......com:/home/ubuntu


## mysql -u root -p 들어가서 비밀번호 변경 user.root 의 접속방식이 소켓이아닌 password 인지 확인 후 
* alter user 'root'@'localhost' identified with mysql_native_password by '변경후 비밀번호';

## sudo apt update 후 sudo apt install npm, nginx 설치

## nginx 설정
* cd /etc/nginx 에서 sites-.... 폴더에 default 파일, 
* vi default 후 gg(첫째줄 이동) shift + V , shift + G 후 d 로 삭제 붙여넣기

## dist 파일은 mv dist /var/www/html 로 이동

## 실행중인 프로세스 확인
* netstat tpln | grep 8080

## 프로세스 강제종료
* kill -9 [process num]

## 스프링 부트 실행
* nohup java -jar [program-name].jar &

## ssl https 적용
*  apt-get install python3-certbot-nginx
* certbot certonly --nginx -d hityundong.com
  server {
  listen 443 ssl http2;
  server_name example.com;

  # ssl 인증서 적용하기
  ssl_certificate /etc/letsencrypt/live/example.com/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/example.com/privkey.pem;

  location / { # location 이후 특정 url을 처리하는 방법을 정의(여기서는 / -> 즉, 모든 request)
  proxy_pass https//localhost:9001; # Request에 대해 어디로 리다이렉트하는지 작성. 8443 -> 자신의 springboot app 이사용하는 포트
  proxy_set_header Host $http_host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto $scheme;
  }
  }

# 메모리
sudo dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo vi /etc/fstab
# vi 맨 아랫줄에 추가
/swapfile swap swap defaults 0 0

