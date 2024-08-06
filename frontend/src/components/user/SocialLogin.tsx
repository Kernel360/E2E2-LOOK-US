import { useEffect } from 'react';
import { Box, Icon, Stack, Typography } from '@mui/material';
import { useState } from 'react';
import { Email } from '@mui/icons-material';
import { Button } from '@mui/material';
import { Divider } from '@mui/material';

import LOGO_NAVER from '@/assets/Naver.png';
import LOGO_KAKAO from '@/assets/KakaoTalk.svg';
import LOGO_GOOGLE from '@/assets/Google.svg';

import {useNavigate} from "react-router";
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { grey, green } from '@mui/material/colors';
import { requestGoogleSignIn } from '@/api/requestGoogleSignIn';

const theme = createTheme({
  palette: {
    primary: {
      main: grey[100],
      contrastText: grey[800],
    },
    secondary: {
      main: grey[800],
      contrastText: '#fff',
    }
  },
});


function IconKakao() {
  return (
    <Icon>
      <img alt={"LOGO_KAKAO"} src={LOGO_KAKAO} className=" align-middle text-center h-full"></img>
    </Icon>
  );
}

function IconGoogle() {
  return (
    <Icon>
      <img alt={"LOGO_GOOGLE"} src={LOGO_GOOGLE} className=" align-middle text-center h-full"></img>
    </Icon>
  );
}

function IconNaver() {
  return (
    <Icon>
      <img alt={"LOGO_NAVER"} src={LOGO_NAVER} className=" align-middle text-center h-full"></img>
    </Icon>
  );
}

export function SocialLogin() {
  const navigate = useNavigate();

  const handleKakaoSignInClick = () => {
    (async () => {
        alert("NOT IMPLEMENTED");
    })(/* IIFE */);
  }

  const handleNaverSignInClick = () => {
    (async () => {
        alert("NOT IMPLEMENTED");
    })(/* IIFE */);
  }

  const handleGoogleSignInClick = () => {
    (async () => {
      await requestGoogleSignIn(); // 이 과정을 거쳐 자동 JWT 토큰이 발급된.
    })(/* IIFE */);
  }

  return (
    <>
      <div className=" flex flex-col justify-center h-screen ">
        
        <ThemeProvider theme={theme}>
          <Box sx={{ margin: "auto" }}>
            <Stack spacing={2} sx={{ /*alignItems: "start", */width: "100%", flex: 1 }}>
              <Typography gutterBottom fontWeight={600} textAlign="center" variant="h2" color={grey[800]} component="div" margin={0}>
                {"Sign In"}
              </Typography>

              <Button
                size='large'
                onClick={handleGoogleSignInClick}
                startIcon={<IconGoogle />}
                variant="contained"
                color='primary'
                sx={{ justifyContent: "start", textTransform: "none" }}
              >
                Continue with Google
              </Button>

              <Button
                size='large'
                onClick={handleKakaoSignInClick}
                startIcon={<IconKakao />}
                variant="contained"
                color='primary'
                sx={{ justifyContent: "start", textTransform: "none" }}
              >
                Continue with Kakao
              </Button>

              <Button
                size='large'
                onClick={handleNaverSignInClick}
                startIcon={<IconNaver />}
                variant="contained"
                color='primary'
                sx={{ justifyContent: "start", textTransform: "none" }}
              >
                Continue with Naver
              </Button>

            </Stack>
          </Box>
        </ThemeProvider>
      </div>
    </>
  );
}
