import { z } from "zod";
import { API_URL } from "./API_URL";
import { formSchemaSignUp } from "@/components/user/SignUpForm";


export interface POST_SignUpRequestFormat {
  nickname: string;
  // email: string;
  // password: string;
  profileImage: Blob;
  birth: Date;
  instaId: string;
  gender: "MAN" | "WOMAN" | "NONE";
}

export interface POST_SignUpResponseFormat {
    // ...
}

/*******************
 *    회원 가입
 *******************/
export async function requestSignUp(
    formData: z.infer<typeof formSchemaSignUp>,
    clientSideImageUrl: string
) {

  // 생일
  // 인스타 id
  // 닉네임 --> 중복되도 된다.
  // 젠더

  const requestUrl = `${API_URL}/signup`;

  const blob = await (await fetch(clientSideImageUrl)).blob();

  const requestPayload: POST_SignUpRequestFormat = {
    nickname: formData.nickname,
    gender: formData.gender,
    birth: formData.birth,
    instaId: formData.instaId,
    profileImage: blob,
  };
 
  const signUpResponse = await fetch(requestUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(requestPayload),
  });

  return signUpResponse;
}