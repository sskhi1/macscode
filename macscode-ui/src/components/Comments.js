import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import '../styles/Comments.css';
import { AuthContext } from '../AuthContext';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const Comments = ({ problemId }) => {
    const { auth } = useContext(AuthContext);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [username, setUsername] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [commentsPerPage] = useState(10);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (auth) {
            const decodedToken = jwtDecode(auth);
            setUsername(decodedToken.sub);
        }

        const fetchComments = async () => {
            try {
                const response = await axios.get(`/discussion-service/discussion/comments/${problemId}`);
                setComments(response.data);
                setIsLoading(false);
            } catch (error) {
                console.error('Error fetching comments', error);
                setIsLoading(false);
            }
        };

        fetchComments();
    }, [problemId, auth]);

    const cleanHtmlContent = (html) => {
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;

        const hasNonEmptyChild = (node) => {
            if (node.nodeType === Node.TEXT_NODE && node.textContent.trim() !== '') {
                return true;
            }
            if (node.nodeType === Node.ELEMENT_NODE) {
                if (node.tagName === 'IMG') {
                    return true; // Always preserve images
                }
                return Array.from(node.childNodes).some(hasNonEmptyChild);
            }
            return false;
        };

        const removeEmptyNodes = (node) => {
            if (node.nodeType === Node.ELEMENT_NODE) {
                const childNodes = Array.from(node.childNodes);
                if (!hasNonEmptyChild(node)) {
                    node.remove();
                    return;
                }
                childNodes.forEach(removeEmptyNodes);
            }
        };

        Array.from(tempDiv.childNodes).forEach(removeEmptyNodes);

        return tempDiv.innerHTML.trim() !== '' ? tempDiv.innerHTML : '';
    };


    const handleAddComment = async () => {
        const cleanedComment = cleanHtmlContent(newComment);
        console.log(cleanedComment)
        if (!cleanedComment) return;

        try {
            const response = await axios.post('/discussion-service/discussion/addComment', {
                problemId,
                comment: cleanedComment,
                username,
            });
            setComments([response.data, ...comments]);
            setNewComment('');
        } catch (error) {
            console.error('Error adding comment', error);
        }
    };

    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(undefined, options);
    };

    const indexOfLastComment = currentPage * commentsPerPage;
    const indexOfFirstComment = indexOfLastComment - commentsPerPage;
    const currentComments = comments.slice(indexOfFirstComment, indexOfLastComment);
    const totalPages = Math.ceil(comments.length / commentsPerPage);

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    if (isLoading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <div className="loading-text">Loading...</div>
            </div>
        );
    }

    return (
        <div className="comments-container">
            <div className="add-comment">
                <ReactQuill
                    value={newComment}
                    onChange={setNewComment}
                    placeholder="Add your comment..."
                    modules={modules}
                    formats={formats}
                />
                <button onClick={handleAddComment}>Submit</button>
            </div>
            <div className="comments-summary">
                <span className="comments-count">Total Comments: {comments.length}</span>
            </div>
            <div className="comments-list">
                {currentComments.map((comment) => (
                    <Comment key={comment.id} comment={comment} username={username} formatDate={formatDate} />
                ))}
            </div>
            <div className="pagination">
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index + 1}
                        className={`pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                        onClick={() => handlePageChange(index + 1)}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    );
};

const Comment = ({ comment, username, formatDate }) => {
    const [replies, setReplies] = useState([]);
    const [newReply, setNewReply] = useState('');
    const [showReplies, setShowReplies] = useState(false);

    const cleanHtmlContent = (html) => {
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;

        const hasNonEmptyChild = (node) => {
            if (node.nodeType === Node.TEXT_NODE && node.textContent.trim() !== '') {
                return true;
            }
            if (node.nodeType === Node.ELEMENT_NODE) {
                if (node.tagName === 'IMG') {
                    return true; // Always preserve images
                }
                return Array.from(node.childNodes).some(hasNonEmptyChild);
            }
            return false;
        };

        const removeEmptyNodes = (node) => {
            if (node.nodeType === Node.ELEMENT_NODE) {
                const childNodes = Array.from(node.childNodes);
                if (!hasNonEmptyChild(node)) {
                    node.remove();
                    return;
                }
                childNodes.forEach(removeEmptyNodes);
            }
        };

        Array.from(tempDiv.childNodes).forEach(removeEmptyNodes);

        return tempDiv.innerHTML.trim() !== '' ? tempDiv.innerHTML : '';
    };

    const fetchReplies = async () => {
        if (!showReplies) {
            try {
                const response = await axios.get(`/discussion-service/discussion/replies/${comment.id}`);
                setReplies(response.data);
            } catch (error) {
                console.error('Error fetching replies', error);
            }
        }
        setShowReplies(!showReplies);
    };

    const handleAddReply = async () => {
        const cleanedReply = cleanHtmlContent(newReply);
        if (!cleanedReply) return;

        try {
            const response = await axios.post('/discussion-service/discussion/addReply', {
                commentId: comment.id,
                reply: cleanedReply,
                username,
            });
            setReplies([...replies, response.data]);
            setNewReply('');
        } catch (error) {
            console.error('Error adding reply', error);
        }
    };

    return (
        <div className="comment">
            <div className="comment-header">
                <span className="comment-username">{comment.username}</span>
                <span className="comment-date">{formatDate(comment.commentDate)}</span>
            </div>
            <div className="comment-body" dangerouslySetInnerHTML={{ __html: comment.comment }}></div>
            <button className="reply-button" onClick={fetchReplies}>
                {showReplies ? 'Hide Replies' : 'Show Replies'}
            </button>
            {showReplies && (
                <div className="replies-section">
                    {replies.map((reply) => (
                        <div key={reply.id} className="reply">
                            <div className="reply-header">
                                <span className="reply-username">{reply.username}</span>
                                <span className="reply-date">{formatDate(reply.replyDate)}</span>
                            </div>
                            <div className="reply-body" dangerouslySetInnerHTML={{ __html: reply.reply }}></div>
                        </div>
                    ))}
                    <div className="add-reply">
                        <ReactQuill
                            value={newReply}
                            onChange={setNewReply}
                            placeholder="Add your reply..."
                            modules={modules}
                            formats={formats}
                        />
                        <button onClick={handleAddReply}>Submit</button>
                    </div>
                </div>
            )}
        </div>
    );
};

const modules = {
    toolbar: [
        [{ 'header': '1' }, { 'header': '2' }],
        ['bold', 'italic', 'underline'],
        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
        ['link', 'image'],
        [{ 'script': 'sub' }, { 'script': 'super' }],
        [{ 'indent': '-1' }, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        ['clean']
    ],
};

const formats = [
    'header', 'font', 'size',
    'bold', 'italic', 'underline',
    'list', 'bullet', 'indent',
    'link', 'image', 'color', 'background',
    'align', 'script', 'direction'
];

export default Comments;
