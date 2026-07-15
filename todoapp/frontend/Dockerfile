FROM node:lts-slim AS builder
WORKDIR /app
COPY . .
# One way to reduce the number of layers in your image (and therfore keep it smaller)
# is to chain commands in a RUN statement
RUN npm install && npm run build

# What we do here is create a multi-stage build. Instead of having one base and performing all actions
# and saving all our data in that environment we let the first environment (the first FROM statement)
# act as our building environment. Any packages we need to build, software we need to download, we can
# do in the build environment. After that we create another "production" environment where we copy
# over only the files we need to run our app. This let's us standardize our build environment within
# the image, but then only keep what is need for runtime in the container when we create it
FROM nginx:alpine

# Here we specify we are copying index.js from the builder environment, not our host environment
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80