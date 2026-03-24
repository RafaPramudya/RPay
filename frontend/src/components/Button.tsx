import {} from 'react'
import "../assets/css/button.css"

interface ButtonProps {
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void
}

const Button = ({onClick}: ButtonProps) => {
  return (
    <button onClick={onClick}>Klik Akuuuu :&gt;</button>
  )
}

export default Button