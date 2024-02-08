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







