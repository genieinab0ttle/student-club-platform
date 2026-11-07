import { useState } from "react";
import "./App.css";

function App() {
  const [meeting, setMeeting] = useState({
    title: "",
    date: "",
    time: "",
    location: "",
  });

  const [createdMeeting, setCreatedMeeting] = useState(null);

  function handleChange(event) {
    setMeeting({
      ...meeting,
      [event.target.name]: event.target.value,
    });
  }

  function handleSubmit(event) {
    event.preventDefault();
    setCreatedMeeting(meeting);
    setMeeting({ title: "", date: "", time: "", location: "" });
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

      {createdMeeting && (
        <section>
          <h2>Meeting Created!</h2>
          <p>{createdMeeting.title}</p>
          <p>{createdMeeting.date} at {createdMeeting.time}</p>
          <p>{createdMeeting.location}</p>
        </section>
      )}
    </main>
  );
}

export default App;