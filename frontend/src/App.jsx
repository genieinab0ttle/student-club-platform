import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [meeting, setMeeting] = useState({
    title: "",
    date: "",
    time: "",
    location: "",
  });

  const [meetings, setMeetings] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/meetings")
      .then((response) => response.json())
      .then((data) => setMeetings(data))
      .catch((error) => console.error(error));
  }, []);

  function handleChange(event) {
    setMeeting({
      ...meeting,
      [event.target.name]: event.target.value,
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/meetings", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(meeting),
      });

      if (!response.ok) {
        throw new Error("Failed to create meeting");
      }

      const savedMeeting = await response.json();

      setMeetings([...meetings, savedMeeting]);
      setMeeting({ title: "", date: "", time: "", location: "" });
    } catch (error) {
      alert("Could not create meeting. Check the backend.");
      console.error(error);
    }
  }

  return (
    <main className="container">
      <h1>Student Club Management</h1>
      <h2>Create a Club Meeting</h2>

      <form onSubmit={handleSubmit}>
        <label>Meeting title</label>
        <input name="title" value={meeting.title} onChange={handleChange} required />

        <label>Date</label>
        <input type="date" name="date" value={meeting.date} onChange={handleChange} required />

        <label>Time</label>
        <input type="time" name="time" value={meeting.time} onChange={handleChange} required />

        <label>Location</label>
        <input name="location" value={meeting.location} onChange={handleChange} required />

        <button type="submit">Create Meeting</button>
      </form>

      <section>
        <h2>All Club Meetings</h2>

        {meetings.map((item) => (
          <div key={item.id}>
            <h3>{item.title}</h3>
            <p>{item.date} at {item.time}</p>
            <p>{item.location}</p>
          </div>
        ))}
      </section>
    </main>
  );
}

export default App;