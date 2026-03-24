import {useState} from 'react'
import Button from '../components/Button'

const Home = () => {
    const [count, setCount] = useState(0)
    function handleClick() {setCount(count + 1)}

    return (
        <>
        <div className="container">
            <h1>Frontend Development Test</h1>
        </div>
        <div className="note">Kamu udah mencet {count} kali</div>
        <Button onClick={handleClick}/>
        </>
    )
}

export default Home