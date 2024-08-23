import { Separator } from '@/components/ui/separator'
import { ProfileForm } from './profile-form'

export default function SettingsProfilePage() {
    return (
        <div className='space-y-6'>
            <div>
                <h3 className='text-lg font-medium'>프로필 정보 수정하기</h3>
                <p className='text-sm text-muted-foreground'>
                    개인 정보를 수정해요
                </p>
            </div>
            <Separator />
            <ProfileForm />
        </div>
    )
}
